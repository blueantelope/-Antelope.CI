// com.antelope.ci.bus.gate.api.BusGateAPI.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.api.base.BusApi;
import com.antelope.ci.bus.server.common.BusSession;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月9日		上午10:59:05 
 */
public class BusGateAPI extends BusApi {
	public BusGateAPI() {
		super();
	}
	
	public BusGateAPI(BusSession session) {
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
}
