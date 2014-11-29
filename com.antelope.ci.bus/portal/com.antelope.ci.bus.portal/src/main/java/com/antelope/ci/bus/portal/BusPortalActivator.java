// com.antelope.ci.bus.portal.BusPortalActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal;

import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.entrance.EntranceManager;
import com.antelope.ci.bus.server.BusCommonServerActivator;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-21		下午12:10:23 
 */
public class BusPortalActivator extends BusCommonServerActivator {
	protected final static String WAITFORSTART_KEY = "bus.portal.start.wait";
	protected final static long WAITFORSTART_DEFAULT = 0;
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#run()
	 */
	@Override
	protected void run() throws CIBusException {
		log4j.info("启动portal");
		server = new BusPortalServer(m_context);
		long waitForStart = getLongProp(WAITFORSTART_KEY, WAITFORSTART_DEFAULT);
		server.setWaitForStart(waitForStart);
		server.start();
		EntranceManager.monitor(m_context, server.getCondition());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusCommonServerActivator#destroy()
	 */
	@Override
	protected void destroy() throws CIBusException {
		if (server != null) {
			server.stop();
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#handleLoadService(java.lang.String, org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	protected void handleLoadService(String clsName, ServiceReference ref,
			Object service) throws CIBusException {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#handleUnloadService(org.osgi.framework.ServiceReference)
	 */
	@Override
	protected void handleUnloadService(ServiceReference ref)
			throws CIBusException {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#handleStopAllService()
	 */
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