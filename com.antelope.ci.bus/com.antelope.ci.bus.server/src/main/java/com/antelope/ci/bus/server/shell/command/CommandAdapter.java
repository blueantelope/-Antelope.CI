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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.CommonBusActivator;
import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		上午10:18:19 
 */
public abstract class CommandAdapter {
	protected static final String DOT = ".";
	protected Map<String, ICommand> commandMap;
	protected CommandType cType;
	protected boolean isQuit;
	
	public CommandAdapter(CommandType cType) {
		this.cType = cType;
		commandMap = new HashMap<String, ICommand>();
		isQuit = false;
		init();
	}
	
	public void addCommand(ICommand command) {
		if (command.getClass().isAnnotationPresent(Command.class)) {
			Command cmd = command.getClass().getAnnotation(Command.class);
			if (cmd.type() == cType) {
				String key = cmd.status() + DOT + cmd.name();
				commandMap.put(key, command);
			}
		}
	}
	
	public void addCommands(String packpath) throws CIBusException {
		List<String>  classList = ClassFinder.findClasspath(packpath, CommonBusActivator.getClassLoader());
		for (String clsname : classList) {
			try {
				Class cls = Class.forName(clsname);
				if (cls.isAssignableFrom(ICommand.class)) {
					ICommand command = (ICommand) cls.newInstance();
					addCommand(command);
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	public void addCommands(String packpath, String status) throws CIBusException {
		List<String>  classList = ClassFinder.findClasspath(packpath, CommonBusActivator.getClassLoader());
		for (String clsname : classList) {
			try {
				Class cls = Class.forName(clsname);
				if (cls.isAssignableFrom(ICommand.class)) {
					ICommand command = (ICommand) cls.newInstance();
					addCommand(command);
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
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
	
	public void execute(String cmd, TerminalIO io, Object... args) throws CIBusException {
		for (String key : commandMap.keySet()) {
			ICommand command = commandMap.get(key);
			if (match(command, cmd)) {
				String status = command.execute(io, args);
				afterExecute(command, status, io, args);
				if (status.equals(BusShellStatus.QUIT))
					isQuit = true;
				break;
			}
		}
	}
	
	protected boolean match(ICommand command, String cmdStr) {
		Command cmd = command.getClass().getAnnotation(Command.class);
		for (String scmd : cmd.commands().split(",")) {
			if (cmdStr.equalsIgnoreCase(scmd.trim()))
				return true;
		}
		
		return false;
	}
	
	public boolean isQuit() {
		return isQuit;
	}
	
	protected abstract void init();
	
	protected abstract void afterExecute(ICommand command, String status, TerminalIO io, Object... args) throws CIBusException;
	
	public abstract void showCommands(TerminalIO io, String prCmd, int width);
}

