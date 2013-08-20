// com.antelope.ci.bus.logger.Logger.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.logger;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.log4j.LogManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import com.antelope.ci.bus.logger.service.BusLogService;
import com.antelope.ci.bus.logger.service.BusLogServiceImpl;


/**
 * 启动日志服务
 * 使用log4j
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		上午10:56:57 
 */
public class BusLogger implements BundleActivator, ServiceListener {
	private BundleContext m_context;
	private BusLogService logService;				// 日志对外服务
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		m_context = context;
		addService();
//		context.addServiceListener(this);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
//		context.removeServiceListener(this);
//		clearService();
	}
	
	/*
	 * 增加对外service
	 */
	private void addService() {
		if (logService == null) {
			String clazz = BusLogService.class.getName();
			logService = new BusLogServiceImpl();
			Dictionary<String, ?> properties = new Hashtable();
			m_context.registerService(clazz, logService, properties);
		}
	}
	
	/*
	 * 清除对外service
	 */
	private void clearService() {
		LogManager.resetConfiguration();			// 关闭log4j日志服务
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.ServiceEvent)
	 */
	@Override
	public void serviceChanged(ServiceEvent event)  {
        String[] objectClass = (String[]) event.getServiceReference().getProperty("objectClass");
        if (event.getType() == ServiceEvent.REGISTERED) {
            System.out.println("Bus Logger : Service of type " + objectClass[0] + " registered.");
        }  else if (event.getType() == ServiceEvent.UNREGISTERING) {
            System.out.println("Bus Logger : Service of type " + objectClass[0] + " unregistered.");
        } else if (event.getType() == ServiceEvent.MODIFIED) {
            System.out.println("Bus Logger: Service of type " + objectClass[0] + " modified.");
        }
    }
}

