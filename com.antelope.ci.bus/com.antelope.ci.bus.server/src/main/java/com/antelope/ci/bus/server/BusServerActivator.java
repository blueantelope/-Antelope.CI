// com.antelope.ci.bus.server.BusCommonServerActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusActivator;
import com.antelope.ci.bus.osgi.ServicePublisher;


/**
 * common server activator, for all ssh sever
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-7		下午6:14:57 
 */
public abstract class BusServerActivator extends BusActivator {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.BusActivator#customInit()
	 */
	@Override
	protected void customInit() throws CIBusException {
		log4j();
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.BusActivator#addServices()
	 */
	@Override
	protected void addServices() throws CIBusException {
		ServicePublisher.publish(m_context, "com.antelope.ci.bus.server.service");
	}
}