// com.antelope.ci.bus.server.command.test.TestBusCommandShellLauncher.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.echo.test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.BusShellLauncher;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-22		下午5:20:56 
 */
public class EchoShellLauncher extends BusShellLauncher {

	@Override
	protected BusShell createShell() throws CIBusException {
		return new EchoShell(createShellSession());
	}

}

