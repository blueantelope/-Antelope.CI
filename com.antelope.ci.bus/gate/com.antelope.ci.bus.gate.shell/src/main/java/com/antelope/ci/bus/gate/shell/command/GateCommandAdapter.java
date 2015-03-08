// com.antelope.ci.bus.gate.core.shell.command.GateCommandAdapter.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.shell.command;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.base.BusShell;
import com.antelope.ci.bus.server.shell.command.ICommand;
import com.antelope.ci.bus.server.shell.command.echo.EchoAdapter;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		上午11:40:18 
 */
public class GateCommandAdapter extends EchoAdapter {
	public static final String NAME = "com.antelope.ci.bus.gate.shell.command.GateCommandAdapter";

	@Override
	protected void initCommands() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void afterExecute(BusShell shell, ICommand command,
			Object... args) throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String userExecute(BusShell shell, boolean refresh, String cmd,
			Object... args) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}

}

