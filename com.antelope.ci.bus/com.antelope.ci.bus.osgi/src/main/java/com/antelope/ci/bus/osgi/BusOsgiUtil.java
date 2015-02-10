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
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.wiring.BundleWiring;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * util of bus osgi
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-13		上午11:33:44 
 */
public class BusOsgiUtil {
	public static List<String> getPackages(String rootPackage) throws CIBusException {
		return ClassFinder.findPacketResource(rootPackage, BusActivator.getClassLoader());
	}
	
	public static Class loadClass(String shellClassName) throws CIBusException {
		Class shellClass;
		try {
			shellClass = ProxyUtil.loadClass(shellClassName);
		} catch (CIBusException e) {
			DevAssistant.assert_exception(e);
			shellClass = ProxyUtil.loadClass(shellClassName, BusActivator.getClassLoader());
		}
		return shellClass;
	}
	
	public static ClassLoader getBundleClassLoader(BundleContext m_context) {
		try { 
			return m_context.getBundle().adapt(BundleWiring.class).getClassLoader();
		} catch (Exception e) {
			DevAssistant.errorln(e);
			return null;
		}
	}
	
	public static ServiceReference<?> getServiceReference(String clazz) {
		return BusActivator.getContext().getServiceReference(clazz);
	}
	
	public static void addServiceToContext(BundleContext m_context, Object service, String serviceName, ServiceProperty... others) {
		String clazz =  service.getClass().getName();
		m_context.registerService(serviceName, service, initServiceProperties(serviceName, clazz, others));
		DevAssistant.assert_out("register service : " + serviceName + ", " + service.getClass().getName());
	}
	
	private static Dictionary initServiceProperties(String serviceName, String className, ServiceProperty... others) {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(BusOsgiConstant.SERVICE_NAME, serviceName);
		properties.put(BusOsgiConstant.SERVICE_CLASS_NAME, className);
		for (ServiceProperty other : others) {
			properties.put(other.getKey(), other.getValue());
		}
		return properties;
	}
	
	public static class ServiceProperty {
		private String key;
		private String value;
		
		public ServiceProperty(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
}
