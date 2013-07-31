// com.antelope.ci.bus.logger.Logger.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.logger;

import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


/**
 * 启动日志服务
 * 使用log4j
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		上午10:56:57 
 */
public class Logger implements BundleActivator {
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Start Logger for @Antelope CI Bus");
		PropertyConfigurator.configure(Logger.class.getResource("/log4j.properties"));
		org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Logger.class);
		log.info("Welcome to Logger World!");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
		// TODO Auto-generated method stub
		
	}

}

