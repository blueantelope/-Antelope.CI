// com.antelope.ci.bus.server.api.BusAPIManager.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api;

import com.antelope.ci.bus.server.api.launcher.BusApiCondition;
import com.antelope.ci.bus.server.api.launcher.BusApiLauncher;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月7日		下午9:51:59 
 */
public class BusApiManager {
	protected BusApiLauncher launcher;

	public BusApiManager(BusApiCondition condition) {
		launcher = new BusApiLauncher(condition);
	}
	
	public BusApiLauncher getLauncher() {
		return launcher;
	}
}

