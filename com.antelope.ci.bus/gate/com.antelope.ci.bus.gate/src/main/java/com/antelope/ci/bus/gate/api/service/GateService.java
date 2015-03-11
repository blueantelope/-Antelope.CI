// com.antelope.ci.bus.gate.service.ShellService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api.service;

import com.antelope.ci.bus.server.api.BusApiManager;
import com.antelope.ci.bus.server.api.launcher.BusApiCondition;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		上午10:12:14 
 */
public class GateService {
	public static final String NAME = "com.antelope.ci.bus.gate.api.service.GateService";
	protected BusApiManager manager;
	
	public GateService(BusApiCondition condition) {
		manager = new BusApiManager(condition);
	}
	
	public BusApiManager getManager() {
		return manager;
	}
}
