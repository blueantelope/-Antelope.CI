// com.antelope.ci.bus.server.command.test.TestBusCommandShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.command.test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.BusBaseCommandShell;
import com.antelope.ci.bus.server.shell.BusShellSession;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-22		下午5:22:04 
 */
public class CommandShell extends BusBaseCommandShell {

	public CommandShell(BusShellSession session) {
		super(session);
	}

	@Override
	protected void custom() throws CIBusException {
		cmdAdapter.addCommand(new HCommand());		
	}

	@Override
	protected void shutdown() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}


	@Override
	protected String prompt() {
		return "[test command]# "; 
	}

	@Override
	protected String header() {
		return null;
	}

}

