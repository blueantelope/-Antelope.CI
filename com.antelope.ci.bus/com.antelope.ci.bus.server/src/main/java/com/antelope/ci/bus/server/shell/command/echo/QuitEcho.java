// com.antelope.ci.bus.server.shell.command.BusQuitCommand.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command.echo;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandType;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-25		下午9:07:41 
 */
@Command(name="quit", commands="quit, exit", status=BusShellStatus.ROOT, type=CommandType.ECHO)
public class QuitEcho implements Echo {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.ICommand#execute(com.antelope.ci.bus.server.shell.core.TerminalIO, java.lang.Object[])
	 */
	@Override
	public String execute(TerminalIO io, Object... args) {
		return BusShellStatus.QUIT;
	}
	
}

