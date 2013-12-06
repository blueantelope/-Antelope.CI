// com.antelope.ci.bus.server.shell.command.CommandAdapter.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command.echo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandAdapter;
import com.antelope.ci.bus.server.shell.command.CommandType;
import com.antelope.ci.bus.server.shell.command.ICommand;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * 交互式命令适配器
 * 所有交互式命令调用由此适配器进入
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-25		下午9:23:21 
 */
public class EchoAdapter extends  CommandAdapter {
	public EchoAdapter() {
		super(CommandType.ECHO);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.CommandAdapter#init()
	 */
	@Override
	protected void init() {
		addCommand(new HelpEcho());
		addCommand(new QuitEcho());
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.CommandAdapter#showCommands(com.antelope.ci.bus.server.shell.core.TerminalIO, java.lang.String, int)
	 */
	@Override
	public void showCommands(TerminalIO io, String prCmd, int width) {
		List<String> cmdList = new ArrayList<String>();
		int maxLen = 0;
		for (String name : commandMap.keySet()) {
			ICommand command = commandMap.get(name);
			Command cmd = command.getClass().getAnnotation(Command.class);
			if (cmd.type() == cType) {
				for (String scmd : cmd.commands().split(",")) {
					if (scmd.contains(prCmd.toLowerCase())) {
						cmdList.add(scmd);
						maxLen = scmd.length() > maxLen ? scmd.length() : maxLen;
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
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.CommandAdapter#afterExecute(com.antelope.ci.bus.server.shell.command.ICommand, java.lang.String, com.antelope.ci.bus.server.shell.core.TerminalIO, java.lang.Object[])
	 */
	@Override
	protected void afterExecute(ICommand command, String status, TerminalIO io, Object... args) throws CIBusException {

	}
}

