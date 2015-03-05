// com.antelope.ci.bus.portal.core.service.ShellService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.service;

import com.antelope.ci.bus.portal.core.shell.SimpleBusPortalShell;
import com.antelope.ci.bus.server.shell.BusShellManager;
import com.antelope.ci.bus.server.shell.launcher.BusShellCondition;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月16日		下午1:04:39 
 */
public class ShellService {
	public static final String NAME = "com.antelope.ci.bus.portal.core.service.ShellService";
	protected BusShellManager manager;
	
	public ShellService(BusShellCondition condition) {
		condition.addDefaultShellClass(SimpleBusPortalShell.class.getName());
		manager = new BusShellManager(condition);
	}
	
	public BusShellManager getManager() {
		return manager;
	}
}
