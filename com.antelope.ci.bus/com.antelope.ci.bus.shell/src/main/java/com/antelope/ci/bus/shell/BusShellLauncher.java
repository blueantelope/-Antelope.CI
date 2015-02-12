// com.antelope.ci.bus.server.shell.BusShellCommand.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.shell;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * create and run bus shell
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-17		下午10:22:10 
 */
public abstract class BusShellLauncher {
	protected BusShellCondition condition;
	
	public BusShellLauncher() {
		super();
	}
	
	public void setCondition(BusShellCondition condition) {
		this.condition = condition;
	}
	
	public abstract BusShell createShell() throws CIBusException;
}

