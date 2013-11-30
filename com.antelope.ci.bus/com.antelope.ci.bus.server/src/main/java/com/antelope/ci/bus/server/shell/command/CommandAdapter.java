// com.antelope.ci.bus.server.shell.command.CommandAdapter.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * 命令适配器
 * 所有命令调用由此适配器进入
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-25		下午9:23:21 
 */
public class CommandAdapter {
	public static final CommandAdapter adapter = new CommandAdapter();
	
	public static final CommandAdapter getAdapter() {
		return adapter;
	}
	
	protected Map<String, Command> commandMap;
	protected boolean isQuit;
	
	private CommandAdapter() {
		commandMap = new HashMap<String, Command>();
		init();
	}
	
	private void init() {
		addCommand(new HelpCommand());
		addCommand(new QuitCommand());
		isQuit = false;
	}
	
	public void addCommand(Command command) {
		if (command.getClass().isAnnotationPresent(ServerCommand.class)) {
			ServerCommand serverCommand = command.getClass().getAnnotation(ServerCommand.class);
			String name = serverCommand.name().toLowerCase().trim();
			commandMap.put(name, command);
		}
	}
	
	public List<String> findCommands(String prCmd) {
		List<String> cmdList = new ArrayList<String>();
		if (StringUtil.empty(prCmd))
			return cmdList;
		for (String name : commandMap.keySet()) {
			Command command = commandMap.get(name);
			ServerCommand serverCommand = command.getClass().getAnnotation(ServerCommand.class);
			for (String scmd : serverCommand.commands().split(",")) {
				if (scmd.contains(prCmd.toLowerCase())) {
					cmdList.add(scmd.toLowerCase().trim());
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
	
	public void showCommands(TerminalIO io, String prCmd, int width) {
		List<String> cmdList = new ArrayList<String>();
		int maxLen = 0;
		for (String name : commandMap.keySet()) {
			Command command = commandMap.get(name);
			ServerCommand serverCommand = command.getClass().getAnnotation(ServerCommand.class);
			for (String scmd : serverCommand.commands().split(",")) {
				if (scmd.contains(prCmd.toLowerCase())) {
					cmdList.add(scmd);
					maxLen = scmd.length() > maxLen ? scmd.length() : maxLen;
				}
			}
		}
		Collections.sort(cmdList, new Comparator<String>() {
			@Override
			public int compare(String c1, String c2) {
				return c1.compareTo(c2);
			}
		});
		int tab = 4;
		int colWidth = maxLen + tab;
		int cols = width / colWidth;
		cols = cols == 0 ? 1: cols;
		int n = 0;
		while (n < cmdList.size()) {
			if (n % cols == 0) {
				try {
					io.println();
				} catch (IOException e) { }
			}
			String cmd = cmdList.get(n);
			try {
				io.println(cmd);
				int cmdSize = cmd.length();
				while (cmdSize < colWidth) {
					io.write(' ');
					cmdSize++;
				}
			} catch (IOException e) { }
			n++;
		}
	}
	
	public void execute(String cmd, TerminalIO io, Object... args) throws CIBusException {
		for (String name : commandMap.keySet()) {
			Command command = commandMap.get(name);
			if (match(command, cmd)) {
				command.execute(io, args);
				if (command instanceof QuitCommand) {
					isQuit = true;
				}
				break;
			}
		}
	}
	
	public boolean isQuit() {
		return isQuit;
	}
	
	private boolean match(Command command, String cmd) {
		ServerCommand serverCommand = command.getClass().getAnnotation(ServerCommand.class);
		for (String scmd : serverCommand.commands().split(",")) {
			if (cmd.equalsIgnoreCase(scmd.trim()))
				return true;
		}
		
		return false;
	}
}

