// com.antelope.ci.bus.osgi.OsgiUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.osgi;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;


/**
 * util
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-13		上午11:33:44 
 */
public class BusOsgiUtil {
	public static void addServiceToContext(BundleContext m_context, Object service, String serviceName) {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(BusOsgiConstants.SERVICE_NAME, serviceName);
		properties.put(BusOsgiConstants.SERVICE_CLASS_NAME, service.getClass().getName());
		m_context.registerService(serviceName, service, properties);
		System.out.println("register service : " + serviceName + ", " + service.getClass().getName());
	}
}

