// com.antelope.ci.bus.server.command.test.HCommand.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.command.test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.ServerCommand;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-28		下午11:00:04 
 */
@ServerCommand(name="h", commands="h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12, h13, h14, h15")
public class HCommand implements Command {

	@Override
	public void execute(TerminalIO io, Object... args) throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

}

