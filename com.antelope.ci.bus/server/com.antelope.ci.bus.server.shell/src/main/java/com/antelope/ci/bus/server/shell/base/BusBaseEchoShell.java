// com.antelope.ci.bus.server.shell.BusCommandShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.base;

import java.io.IOException;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.NetVTKey;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.common.BusSession;
import com.antelope.ci.bus.server.shell.buffer.BusShellEchoBuffer;


/**
 * 交互式shell
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-22		下午8:30:50 
 */
@Shell(name="base.echo", commandAdapter="com.antelope.ci.bus.server.shell.command.echo.EchoAdapter")
public abstract class BusBaseEchoShell extends BusShell {
	private boolean tabPress;
	
	public BusBaseEchoShell() {
		super();
		initForEcho();
	}
	
	public BusBaseEchoShell(BusSession session) {
		super(session);
		initForEcho();
	}
	
	protected void initForEcho() {
		activeMoveAction = true;
		activeEditAction = true;
		activeUserAction = true;
		tabPress = false;
	}
	
	protected void resetCommand() {
		input.reset();
		tabPress = false;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.base.BusShell#action(int)
	 */
	@Override protected void action(int c) throws CIBusException {
		try {
			input.put((char) c);
		} catch (Exception e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}
	
	private void matchCommand() {
		if (!input.exitSpace()) {
			List<String> cmdList = commandAdapter.findCommands(input.value());
			input.printTips(cmdList, ((BusShellSession)session).getWidth());
		}
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.base.BusShell#mainView()
	 */
	@Override
	public void mainView() throws CIBusException {
		try {
			input = new BusShellEchoBuffer(io, prompt().length());
			if (!StringUtil.empty(header()))
				io.println(header());
			io.write(prompt());
			resetCommand();
		} catch (IOException e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.base.BusShell#userAction(int)
	 */
	@Override protected boolean userAction(int c) throws CIBusException {
		try {
			switch (c) {
				case NetVTKey.TABULATOR:
					if (!input.inCmdTab()) {
						matchCommand();
						if (tabPress) {
							input.tabTip();
							tabPress = false;
						}
						if (!tabPress)
							tabPress = true;
					}
					return true;
				case NetVTKey.LF:
					cmdArg = input.enter();
					if (cmdArg != null) {
						execute(cmdArg);
						resetCommand();
						io.write(prompt());
					}
					return true;
				default:
					return false;
			}
		} catch (Exception e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}
	
	protected abstract String prompt();
	
	protected abstract String header();
}

