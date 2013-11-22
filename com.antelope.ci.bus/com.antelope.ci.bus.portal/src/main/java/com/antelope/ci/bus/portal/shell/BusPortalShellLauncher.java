// com.antelope.ci.bus.portal.shell.BusPortalShellCommand.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.shell;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.BusShellLauncher;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-29		下午9:18:22 
 */
public class BusPortalShellLauncher extends BusShellLauncher {

	@Override
	protected BusShell createShell() throws CIBusException {
		return new BusPortalShell(createShellSession());
	}

}

