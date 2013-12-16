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
import com.antelope.ci.bus.server.shell.buffer.BusEchoBuffer;
import com.antelope.ci.bus.server.shell.buffer.ShellCommandArg;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * 交互式shell
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-22		下午8:30:50 
 */
@Shell(commandAdapter="com.antelope.ci.bus.server.shell.command.echo.EchoAdapter")
public abstract class BusBaseEchoShell extends BusShell {
	protected BusEchoBuffer buffer;
	private boolean tabPress;
	
	public BusBaseEchoShell() {
		super();
		tabPress = false;
	}
	
	public BusBaseEchoShell(BusShellSession session) {
		super(session);
		tabPress = false;
	}
	
	protected void resetCommand() {
		buffer.reset();
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
				if (keyBell)
					io.bell();
				switch (c) {
					case TerminalIO.LEFT:
						buffer.left();
						break;
					case TerminalIO.RIGHT:
						buffer.right();
						break;
					case TerminalIO.UP:
						buffer.up();
						break;
					case TerminalIO.DOWN:
						buffer.down();
						break;
					case TerminalIO.DELETE:
						buffer.delete();
						break;
					case TerminalIO.BACKSPACE:
						buffer.backspace();
						break;
					case TerminalIO.SPACE:
						buffer.space();
						break;
					case TerminalIO.TABULATOR:
						if (!buffer.inCmdTab()) {
							matchCommand();
							if (tabPress) {
								buffer.tabTip();
								tabPress = false;
							}
							if (!tabPress)
								tabPress = true;
						}
						break;
					case TerminalIO.ENTER:
						ShellCommandArg cmdArg = buffer.enter();
						if (cmdArg != null) {
							execute(cmdArg);
							resetCommand();
							io.write(prompt());
						}
						break;
					default:
						buffer.put((char) c);
						break;
				}
			}
		} catch (Exception e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}
	
	private void matchCommand() {
		if (!buffer.exitSpace()) {
			List<String> cmdList = commandAdapter.findCommands(buffer.read());
			buffer.printTips(cmdList, session.getWidth());
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
			buffer = new BusEchoBuffer(io, prompt().length());
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

