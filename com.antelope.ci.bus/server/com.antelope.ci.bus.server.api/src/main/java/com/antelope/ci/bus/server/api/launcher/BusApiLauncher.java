// com.antelope.ci.bus.server.api.launcher.BusAPILauncher.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.launcher;

import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.api.base.BusApi;
import com.antelope.ci.bus.server.common.BusChannel;
import com.antelope.ci.bus.server.common.BusLauncher;
import com.antelope.ci.bus.server.common.BusSession;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		下午4:23:24 
 */
public class BusApiLauncher extends BusLauncher {
	public BusApiLauncher() {
		super();
	}
	
	public BusApiLauncher(BusApiCondition condition) {
		super(condition);
	}
	
	protected String getApiClass() {
		if (condition == null)
			return BusApiCondition.DEFAULT_API;
		return ((BusApiCondition) condition).getApiClass();
	}

	@Override
	public BusChannel launch(BusSession session) throws CIBusException {
		String apiClass = getApiClass();
		BusApi api = (BusApi) ProxyUtil.newObject(apiClass);
		if (api == null)
			api = (BusApi) ProxyUtil.newObject(apiClass, condition.getClassLoader());
		api.setContext(condition.getContexts());
		api.attatchSession(session);
		return api;
	}
}
