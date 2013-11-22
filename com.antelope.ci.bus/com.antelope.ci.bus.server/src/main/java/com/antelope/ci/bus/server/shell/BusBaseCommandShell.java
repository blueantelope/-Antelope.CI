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


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-22		下午8:30:50 
 */
public abstract class BusBaseCommandShell extends BusShell {
	private static final int command_buf_size = 1024;
	protected CharBuffer command_buf;
	
	public BusBaseCommandShell(BusShellSession session) {
		super(session);
		command_buf = CharBuffer.allocate(command_buf_size);
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
				io.write((byte) c);
				if (c == 10) {
					command_buf.flip();
					String command = command_buf.toString();
					command_buf.clear();
					if ("exit".equalsIgnoreCase(command) || "quit".equalsIgnoreCase(command)) {
						quit = true;
					} else {
						execute(command);
						io.println();
						io.write(prompt());
					}
				} else {
					command_buf.put((char) c);
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
		} catch (IOException e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}
	
	protected abstract String prompt();
	
	protected abstract String header();
	
	protected abstract  void execute(String command) throws CIBusException;
}

