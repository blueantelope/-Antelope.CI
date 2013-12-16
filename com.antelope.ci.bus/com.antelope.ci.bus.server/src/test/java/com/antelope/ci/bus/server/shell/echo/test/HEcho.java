// com.antelope.ci.bus.server.command.test.HCommand.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.echo.test;

import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.BaseCommand;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandType;
import com.antelope.ci.bus.server.shell.command.echo.Echo;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-28		下午11:00:04 
 */
@Command(name="h", commands="h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12, h13, h14, h15", type=CommandType.ECHO)
public class HEcho extends BaseCommand implements Echo {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.BaseCommand#execute(com.antelope.ci.bus.server.shell.core.TerminalIO, java.lang.Object[])
	 */
	@Override
	public String execute(TerminalIO io, Object... args) {
		return BusShellStatus.KEEP;
	}

}

