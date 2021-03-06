// com.antelope.ci.bus.engine.model.BusEngineModelActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model;

import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusActivator;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月24日		下午5:30:37 
 * @Deprecated replace by {@link #com.antelope.ci.bus.engine.model.BusEngineModelComplexActivator}
 */
@Deprecated
public class BusEngineModelActivator extends BusActivator {

	@Override
	protected void customInit() throws CIBusException {
		
	}

	@Override
	protected void run() throws CIBusException {
		
	}

	@Override
	protected void destroy() throws CIBusException {
		
	}

	@Override
	protected void handleLoadService(String clsName, ServiceReference ref, Object service) throws CIBusException {
		
	}

	@Override
	protected void handleUnloadService(ServiceReference ref) throws CIBusException {
		
	}

	@Override
	protected void handleStopAllService() throws CIBusException {
		
	}

	@Override
	protected void publishServices() throws CIBusException {
		
	}

	@Override
	protected void removeServices() throws CIBusException {
		
	}

	@Override
	protected String[] customLoadServices() {
		return null;
	}
}
