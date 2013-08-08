// com.antelope.ci.bus.test.Test.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.logger.test;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import com.antelope.ci.bus.logger.test.service.TestLogService;
import com.antelope.ci.bus.logger.test.service.TestLogServiceImpl;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-6-8		上午10:21:15 
 */
public class TestLogger implements BundleActivator, ServiceListener {

	@Override
	public void start(BundleContext context) throws Exception {
		context.addServiceListener(this);
		System.out.println("Logger Test for @Antelope CI");
		PropertyConfigurator.configure(TestLogger.class.getResourceAsStream("/log4j.properties"));
		Logger log = Logger.getLogger(TestLogger.class);
		log.info("Welcome to Logger World!");
		String clazz = TestLogService.class.getName();
		TestLogService logService = new TestLogServiceImpl();
		Dictionary<String, ?> properties = new Hashtable();
		context.registerService(clazz, logService, properties);
		log.info("启动完成");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.ServiceEvent)
	 */
	@Override
	public void serviceChanged(ServiceEvent event) {
		System.out.println("监控service");
		String[] objectClass = (String[]) event.getServiceReference().getProperty("objectClass");
        if (event.getType() == ServiceEvent.REGISTERED) {
            System.out.println("Test Logger : Service of type " + objectClass[0] + " registered.");
        }  else if (event.getType() == ServiceEvent.UNREGISTERING) {
            System.out.println("Test Logger : Service of type " + objectClass[0] + " unregistered.");
        } else if (event.getType() == ServiceEvent.MODIFIED) {
            System.out.println("Test Logger: Service of type " + objectClass[0] + " modified.");
        }
	}

}

