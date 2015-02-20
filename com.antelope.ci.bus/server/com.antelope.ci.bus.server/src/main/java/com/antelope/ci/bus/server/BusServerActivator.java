// com.antelope.ci.bus.server.BusCommonServerActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.ServicePublisher;


/**
 * common server activator, for all ssh sever
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-7		下午6:14:57 
 */
public class BusServerActivator extends BusServerTemplateActivator {
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.BusActivator#publishServices()
	 */
	@Override
	protected void publishServices() throws CIBusException {
		ServicePublisher.publish(bundle_context, "com.antelope.ci.bus.server.service");
	}

	@Override
	protected void run() throws CIBusException {
		
	}

	@Override
	protected void destroy() throws CIBusException {
		
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
	protected void removeServices() throws CIBusException {
		
	}

	@Override
	protected String[] customLoadServices() {
		return null;
	}

	@Override
	protected void customInit() throws CIBusException {
		
	}
}
