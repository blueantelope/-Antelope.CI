// com.antelope.ci.bus.server.service.ServiceManager.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.osgi.BusOsgiUtil;


/**
 * 对外提供servcie管理
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		下午12:31:45 
 */
public class ServicePublisher {
	private static final Logger log = Logger.getLogger(ServicePublisher.class);
	private static List<String> serviceList = new Vector<String>();
	private static List<String> scanedList = new Vector<String>();

	public static void publish(BundleContext m_context) {
		new ServicePublishHook(m_context).start();
	}
	
	private static class ServicePublishHook extends Thread {
		private BundleContext m_context;
		private ServicePublishHook(BundleContext m_context) {
			this.m_context = m_context;
		}
		
		public void run() {
			while (true) {
				String cls_name = "";
				try {
					List<String>  classList = ClassFinder.findClasspath("com.antelope.ci.bus.server.service", 
							BusOsgiUtil.getBundleClassLoader(m_context));
					for (String cls : classList) {
						cls_name = cls;
						boolean scaned = false;
						for (String scanedCls : scanedList) {
							if (scanedCls.equals(cls)) {
								scaned = true;
								break;
							}
						}
						if (!scaned) {
							scanedList.add(cls);
							Class clazz = Class.forName(cls, false, BusOsgiUtil.getBundleClassLoader(m_context));
							if (Service.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(ServerService.class)) {
								ServerService ss =  (ServerService) clazz.getAnnotation(ServerService.class);
								Service service = (Service) clazz.newInstance();
								String serviceName = ss.serviceName();
								BusOsgiUtil.addServiceToContext(m_context, service, serviceName);
								serviceList.add(cls);
								log.info("add service :" + cls_name);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.warn("problem for add service :" + cls_name);
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}

