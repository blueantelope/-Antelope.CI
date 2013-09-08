// com.antelope.ci.bus.server.MainServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.logger.service.BusLogService;
import com.antelope.ci.bus.osgi.CommonBusActivator;
import com.antelope.ci.bus.server.BusServerConfig.KT;

/**
 * 持续bus总线服务
 * 使用ssh方式
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-30		下午11:23:33 
 */
public class BusServerActivator extends CommonBusActivator {
	private static Logger log4j;			// log4j
	private BusServer server;

	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#run()
	 */
	@Override
	protected void run() throws CIBusException {
		try {
			server = new BusServer();
			BusServerConfig config = BusServerConfig.fromProps(properties);
			if (config.getKt() == KT.DYNAMIC)
				config.setKey_url(getResource(config.getKey_name()));
			server.setConfig(config);
			server.start();
		} catch (IOException e) {
			throw new CIBusException("", e);
		}

	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#destroy()
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
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#handleLoadService()
	 */
	@Override
	protected void handleLoadService() throws CIBusException {
		if (logService != null) {
			log4j = ((BusLogService) logService).getLog4j(BusServerActivator.class);
			log4j.info("得到Bus Log Service");
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#handleUnloadService(org.osgi.framework.ServiceReference)
	 */
	@Override
	protected void handleUnloadService(ServiceReference ref)
			throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#handleStopAllService()
	 */
	@Override
	protected void handleStopAllService() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#addServices()
	 */
	@Override
	protected void addServices() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#removeServices()
	 */
	@Override
	protected void removeServices() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}
	
}

