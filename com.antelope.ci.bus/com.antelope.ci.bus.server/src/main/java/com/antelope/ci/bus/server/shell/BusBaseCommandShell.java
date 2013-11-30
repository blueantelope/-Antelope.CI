// com.antelope.ci.bus.server.shell.BusCommandShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.io.IOException;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.BusCommandBuffer.CommandArgs;
import com.antelope.ci.bus.server.shell.command.CommandAdapter;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * 命令式shell
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-22		下午8:30:50 
 */
public abstract class BusBaseCommandShell extends BusShell {
	protected BusCommandBuffer cmdBuf;
	protected CommandAdapter cmdAdapter;
	private boolean tabPress;
	
	public BusBaseCommandShell(BusShellSession session) {
		super(session);
		cmdAdapter = CommandAdapter.getAdapter();
		tabPress = false;
	}
	
	protected void resetCommand() {
		cmdBuf.reset();
		tabPress = false;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#action()
	 */
	@Override
	protected void action() throws CIBusException {
		try {
			int c = io.read();
			if (c != -1) {
				switch (c) {
					case TerminalIO.LEFT:
						cmdBuf.left();
						break;
					case TerminalIO.RIGHT:
						cmdBuf.right();
						break;
					case TerminalIO.UP:
						
						break;
					case TerminalIO.DOWN:
						break;
					case TerminalIO.DELETE:
						cmdBuf.delete();
						break;
					case TerminalIO.BACKSPACE:
						cmdBuf.backspace();
						break;
					case TerminalIO.BLANK:
						cmdBuf.put((char) c);
						cmdBuf.addBlank();
						break;
					case TerminalIO.TABULATOR:
						if (!cmdBuf.inCmdTab()) {
							matchCommand();
							if (tabPress) {
								cmdBuf.tabTip();
								tabPress = false;
							}
							if (!tabPress)
								tabPress = true;
						}
						break;
					case TerminalIO.ENTER:
						if (cmdBuf.inCmdTab()) {
							cmdBuf.enterTip();
							cmdBuf.clearTips();
						} else {
							CommandArgs cmdArgs = cmdBuf.enter((char) c);
							cmdAdapter.execute(cmdArgs.getCommand(), io, cmdArgs.getArgs());
							if (cmdAdapter.isQuit()) {
								quit = true;
							} else {
								io.write(prompt());
							}
							resetCommand();
						}
						break;
					default:
						cmdBuf.put((char) c);
						break;
				}
			}
		} catch (Exception e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}
	
	private void matchCommand() {
		if (!cmdBuf.exitBlank()) {
			List<String> cmdList = cmdAdapter.findCommands(cmdBuf.read());
			cmdBuf.printTips(cmdList, session.getWidth());
		}
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#mainView()
	 */
	@Override
	protected void mainView() throws CIBusException {
		try {
			cmdBuf = new BusCommandBuffer(io, prompt().length());
			if (!StringUtil.empty(header()))
				io.println(header());
			io.write(prompt());
			resetCommand();
		} catch (IOException e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}
	
	protected abstract String prompt();
	
	protected abstract String header();
}

