// com.antelope.ci.bus.gate.service.ShellService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.baseservice;

import com.antelope.ci.bus.server.api.BusAPIManager;
import com.antelope.ci.bus.server.api.launcher.BusAPICondition;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		上午10:12:14 
 */
public class GateService {
	public static final String NAME = "com.antelope.ci.bus.gate.baseservice.GateService";
	protected BusAPIManager manager;
	
	public GateService(BusAPICondition condition) {
		manager = new BusAPIManager(condition);
	}
	
	public BusAPIManager getManager() {
		return manager;
	}
}
