// com.antelope.ci.bus.gate.BusGateServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.BusServer;
import com.antelope.ci.bus.server.BusServerCondition;
import com.antelope.ci.bus.server.BusServerConfig;


/**
 * gate server to run on ssh server
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年1月31日		下午1:14:49 
 */
public class BusGateServer extends BusServer {
	public BusGateServer(ClassLoader classloader) throws CIBusException {
		super(classloader);
	}
	
	@Override
	protected void customizeConfig(BusServerConfig config)
			throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void attatchCondition(BusServerCondition server_condition)
			throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void customizeInit() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void start() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void shutdown() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toSummary() {
		
		// TODO Auto-generated method stub
		return null;
		
	}

}
