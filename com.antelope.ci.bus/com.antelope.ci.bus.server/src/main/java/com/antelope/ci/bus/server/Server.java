// com.antelope.ci.bus.server.MainServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * 持续bus总线服务
 * 使用ssh方式
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-30		下午11:23:33 
 */
public class Server implements BundleActivator {
	private static final Logger log = Logger.getLogger(Server.class);			// log4j
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		log.info("Bus Server Start...");
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		
		// TODO Auto-generated method stub
		
	}

}

