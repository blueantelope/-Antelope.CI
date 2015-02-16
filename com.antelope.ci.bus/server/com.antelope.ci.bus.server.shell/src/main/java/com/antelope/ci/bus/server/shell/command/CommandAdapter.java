// com.antelope.ci.bus.server.shell.command.CommandAdapter.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.base.BusShell;
import com.antelope.ci.bus.server.shell.base.BusShellStatus;
import com.antelope.ci.bus.server.shell.base.BusShellStatus.BaseStatus;
import com.antelope.ci.bus.server.shell.command.CommandHelper.COMMAND_SIGN;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		上午10:18:19 
 */
public abstract class CommandAdapter {
	private static final Logger log = Logger.getLogger(CommandAdapter.class);
	protected static final String DOT = ".";
	protected Map<String, ICommand> commandMap;
	protected Map<String, ICommand> globalCommandMap;
	protected CommandType cType;
	protected boolean isQuit;
	protected boolean userFinal;
	protected ClassLoader classLoader;
	
	public CommandAdapter(CommandType cType) {
		this.cType = cType;
		globalCommandMap = new ConcurrentHashMap<String, ICommand>();
		commandMap = new ConcurrentHashMap<String, ICommand>();
		isQuit = false;
		init();
		userFinal = false;
		this.classLoader = Thread.currentThread().getContextClassLoader();
	}
	
	public void initClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	public void closeUserFinal() {
		userFinal = false;
	}
	
	public void addCommand(ICommand command) {
		if (command.getClass().isAnnotationPresent(Command.class)) {
			Command cmd = command.getClass().getAnnotation(Command.class);
			if (cmd.type() == cType) {
				BaseStatus bs = BaseStatus.toStatus(cmd.status());
				String key = makeKey(cmd.status(), cmd.mode(), cmd.name());
				if (bs == BaseStatus.GLOBAL)
					globalCommandMap.put(key, command);
				else
					commandMap.put(key, command);
			}
		}
	}
	
	public void loadCommands(String packpath) throws CIBusException {
		List<String>  classList = ClassFinder.findClasspath(packpath, classLoader);
		for (String clsname : classList) {
			try {
				loadCommand(classLoader, clsname);
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	public void loadCommands(String packpath, String status) throws CIBusException {
		List<String>  classList = ClassFinder.findClasspath(packpath, classLoader);
		for (String clsname : classList) {
			loadCommand(classLoader, clsname);
			try {
				loadCommand(classLoader, clsname);
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	public void loadCommand(String clsname) throws CIBusException {
		loadCommand(classLoader, clsname);
	}
	
	private void loadCommand(ClassLoader classloader, String clsname) throws CIBusException {
		Class cmdClass = ProxyUtil.loadClass(clsname, classloader);
		if (cmdClass == null) {
			log.warn("unable load command class : " + clsname);
			return;
		}
		
		if (ICommand.class.isAssignableFrom(cmdClass) && cmdClass.isAnnotationPresent(Command.class)) {
			try {
				ICommand command = (ICommand) cmdClass.newInstance();
				addCommand(command);
			} catch (Exception e) {
				throw new CIBusException("", "add command [" + cmdClass + "] error\n", e);
			}
		}
	}
	
	public List<String> findCommands(String prCmd) {
		List<String> cmdList = new ArrayList<String>();
		if (StringUtil.empty(prCmd))
			return cmdList;
		for (String key : commandMap.keySet()) {
			ICommand command = commandMap.get(key);
			Command cmd = command.getClass().getAnnotation(Command.class);
			if (cmd.type() == cType) {
				for (String scmd : cmd.commands().split(",")) {
					if (scmd.toLowerCase().trim().startsWith(prCmd.toLowerCase())) {
						cmdList.add(scmd.toLowerCase().trim());
					}
				}
			}
		}
		Collections.sort(cmdList, new Comparator<String>() {
			@Override
			public int compare(String c1, String c2) {
				return c1.compareTo(c2);
			}
		});
		return cmdList;
	}
	
	public String execute(BusShell shell, boolean refresh, String key) throws CIBusException {
		ICommand command = globalCommandMap.get(key);
		if (command == null)
			command = commandMap.get(key);
		if (command != null) {
			String rs = execute(command, shell, refresh, new String[]{});
			if (rs != null)
				return rs;
		}
		
		return BusShellStatus.KEEP;
	}
	
	public String execute(BusShell shell, boolean refresh, String cmd, Object... args) throws CIBusException {
		String rs = userExecute(shell, refresh, cmd, args);
		if (rs != null)	return rs;
		if (userFinal)	return null;
		
		String status = shell.getStatus();
		String mode = shell.getMode();
		
		for (String key : commandMap.keySet()) {
			if (key.contains(status + DOT + mode)) {
				rs = execute(key, commandMap, shell, refresh, cmd, args);
				if (rs != null)
					return rs;
			}
		}
		
		for (String key : globalCommandMap.keySet()) {
			rs = execute(key, globalCommandMap, shell, refresh, cmd, args);
			if (rs != null)
				return rs;
		}
		
		return BusShellStatus.KEEP;
	}
	
	protected String execute(String key, Map<String, ICommand> currentCmdMap, 
			BusShell shell, boolean refresh, String cmd, Object... args) throws CIBusException {
		String result = null;
		ICommand command = currentCmdMap.get(key);
		if (command != null && match(command, cmd))
			result =  execute(command, shell, refresh, cmd, args);
		if (result != null)
			shell.clearData();
		
		return result;
	}
	
	protected String execute(ICommand command,  BusShell shell, boolean refresh, Object... args) throws CIBusException {
		String actionStatus = command.execute(refresh, shell, args);
		afterExecute(shell, command, args);
		return actionStatus;
	}
	
	protected boolean match(ICommand command, String cmdStr) {
		Command cmd = command.getClass().getAnnotation(Command.class);
		for (String scmd : cmd.commands().split(",")) {
			if (StringUtil.empty(scmd)) continue;
			if (scmd.length() > 1) 		scmd = scmd.trim();
			
			StringBuffer cmdBuf = new StringBuffer();
			String[] cmdEntries = CommandHelper.split(scmd);
			for (String cmdEntry : cmdEntries) {
				COMMAND_SIGN csign = COMMAND_SIGN.toSign(cmdEntry);
				switch (csign) {
					case NUMBER:
						String c = csign.truncate(cmdEntry);
						if (StringUtil.isNumeric(c))
							cmdBuf.append((char) Integer.parseInt(c));
						break;
					default:
						cmdBuf.append(cmdEntry);
						break;
				}
			}
			if (cmdBuf.toString().equals(cmdStr))
				return true;
		}
		return false;
	}
	
	public boolean isQuit() {
		return isQuit;
	}
	
	protected String makeKey(String status, String mode, String name) {
		return status + DOT + mode + DOT + name;
	}
	
	protected abstract void init();
	
	protected abstract void initCommands() throws CIBusException;
	
	protected abstract void afterExecute(BusShell shell, ICommand command,  Object... args) throws CIBusException;
	
	public abstract void showCommands(BusShell shell, String prCmd, int width);
	
	protected abstract String userExecute(BusShell shell, boolean refresh, String cmd, Object... args) throws CIBusException;
}

