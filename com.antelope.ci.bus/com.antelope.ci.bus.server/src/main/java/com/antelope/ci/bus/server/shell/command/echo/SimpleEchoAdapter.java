// com.antelope.ci.bus.server.shell.command.echo.SimpleEchoAdapter.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command.echo;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.command.ICommand;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月6日		下午5:51:28 
 */
public class SimpleEchoAdapter extends EchoAdapter {

	@Override
	protected void afterExecute(BusShell shell, ICommand command, Object... args) throws CIBusException {

	}

	@Override
	protected String userExecute(BusShell shell, boolean refresh, String cmd, Object... args) throws CIBusException {
		return null;
	}
}