// com.antelope.ci.bus.gate.service.ShellService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.core.service;

import com.antelope.ci.bus.gate.core.shell.BusGateShell;
import com.antelope.ci.bus.server.shell.BusShellManager;
import com.antelope.ci.bus.server.shell.launcher.BusShellCondition;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		上午10:12:14 
 */
public class ShellService {
	public static final String NAME = "com.antelope.ci.bus.gate.core.service.ShellService";
	protected BusShellManager manager;
	
	public ShellService(BusShellCondition condition) {
		condition.addDefaultShellClass(BusGateShell.class.getName());
		manager = new BusShellManager(condition);
	}
	
	public BusShellManager getManager() {
		return manager;
	}
}
