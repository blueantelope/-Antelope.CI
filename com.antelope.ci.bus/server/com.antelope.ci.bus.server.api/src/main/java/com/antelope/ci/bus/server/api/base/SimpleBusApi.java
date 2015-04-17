// com.antelope.ci.bus.server.api.base.SimpleBusAPI.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.base;

import com.antelope.ci.bus.common.api.ApiMessage;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月8日		下午12:55:58 
 */
public class SimpleBusApi extends BusApi {
	public SimpleBusApi() {
		super();
	}
	
	public SimpleBusApi(BusApiSession session) {
		super(session);
	}

	@Override
	protected void customApiEnv() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void init() {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void release() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleInMessage(ApiMessage message) {
		
		// TODO Auto-generated method stub
		
	}
}
