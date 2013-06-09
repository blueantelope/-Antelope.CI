// com.antelope.ci.bus.test.Test.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.logger.test;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-6-8		上午10:21:15 
 */
public class TestLogger implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Logger Test for @Antelope CI");
		PropertyConfigurator.configure(TestLogger.class.getResourceAsStream("/log4j.properties"));
		Logger log = Logger.getLogger(TestLogger.class);
		log.info("Welcome to Logger World!");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
		// TODO Auto-generated method stub
		
	}

}

