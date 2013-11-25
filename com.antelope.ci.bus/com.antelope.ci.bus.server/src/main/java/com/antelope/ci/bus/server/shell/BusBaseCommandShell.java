// com.antelope.ci.bus.server.shell.BusCommandShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.io.IOException;
import java.nio.CharBuffer;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.command.CommandAdapter;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-22		下午8:30:50 
 */
public abstract class BusBaseCommandShell extends BusShell {
	private static final int command_buf_size = 1024;
	protected CharBuffer inputBuf;
	protected boolean _32Click;
	protected int cmdIndex;
	protected int cursorIndex;
	
	public BusBaseCommandShell(BusShellSession session) {
		super(session);
		inputBuf = CharBuffer.allocate(command_buf_size);
	}
	
	protected void resetCommand() {
		_32Click = false;
		cmdIndex = prompt().length();
		cursorIndex = prompt().length();
	}
	

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#action()
	 */
	@Override
	protected void action() throws CIBusException {
		CommandAdapter cmdAdapter = CommandAdapter.getAdapter();
		try {
			int c = io.read();
			if (c != -1) {
				switch (c) {
					case TerminalIO.LEFT:
						if (cmdIndex > prompt().length()) {
							sendLeft();
							cursorIndex--;
						}
						break;
					case TerminalIO.RIGHT:
						if (cursorIndex < cmdIndex) {
							sendRight();
							cursorIndex++;
						}
						break;
					case TerminalIO.DELETE:
						sendDelete();
						cmdIndex--;
						break;
					case TerminalIO.BACKSPACE:
						if (cmdIndex > prompt().length()) {
							sendBackspace();
							cmdIndex--;
							cursorIndex--;
						}
						break;
					case 32:
						io.write((byte) c);
						_32Click = true;
						cmdIndex++;
						cursorIndex++;
						break;
					case TerminalIO.TABULATOR:
						if (!_32Click) {
							inputBuf.mark();
							inputBuf.flip();
							cmdAdapter.showCommands(io, inputBuf.toString(), session.getWidth());
							inputBuf.reset();
							inputBuf.limit(inputBuf.capacity());
						}
						break;
					case TerminalIO.ENTER:
						io.write((byte) c);
						inputBuf.flip();
						String line = inputBuf.toString();
						inputBuf.clear();
						String[] strs = line.split(" ");
						String command = strs[0];
						String[] args = new String[strs.length-1];
						int n = 0;
						while (n < args.length) {
							args[n] = strs[++n];
						}
						cmdAdapter.execute(command, io, args);
						if (cmdAdapter.isQuit()) {
							quit = true;
						} else {
							io.println();
							io.write(prompt());
						}
						resetCommand();
						break;
					default:
						io.write((byte) c);
						inputBuf.put((char) c);
						cmdIndex++;
						cursorIndex++;
						break;
				}
			}
		} catch (Exception e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
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

