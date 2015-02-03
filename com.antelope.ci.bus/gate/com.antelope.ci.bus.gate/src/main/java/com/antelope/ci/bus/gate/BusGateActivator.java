// com.antelope.ci.bus.gate.BusGateActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate;

import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.BusCommonServerActivator;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年12月13日		下午8:00:59 
 */
public class BusGateActivator extends BusCommonServerActivator {

	@Override
	protected void run() throws CIBusException {
		log4j.info("启动gate");
		server = new BusGateServer(m_context);
		server.start();
	}

	@Override
	protected void handleLoadService(String clsName, ServiceReference ref,
			Object service) throws CIBusException {
		
		
	}

	@Override
	protected void handleUnloadService(ServiceReference ref)
			throws CIBusException {
		
	}

	@Override
	protected void handleStopAllService() throws CIBusException {
		
	}

	@Override
	protected void addServices() throws CIBusException {
		
	}

	@Override
	protected void removeServices() throws CIBusException {
		
	}
}
