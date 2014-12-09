// com.antelope.ci.bus.server.service.ServiceManager.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.osgi.ServicePublishHook;


/**
 * 对外提供servcie管理
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		下午12:31:45 
 */
public class BusServerServicePublisher {
	private static final Logger log = Logger.getLogger(BusServerServicePublisher.class);
	
	public static void publish(BundleContext m_context) {
		new ServicePublishHook(m_context, "com.antelope.ci.bus.server.service") {
			@Override protected ServicePublishInfo fetchService(Class clazz) {
				ServicePublishInfo info = new ServicePublishInfo();
				if (BusServerService.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(ServerService.class)) {
					try {
						ServerService ss =  (ServerService) clazz.getAnnotation(ServerService.class);
						BusServerService service = (BusServerService) clazz.newInstance();
						String serviceName = ss.serviceName();
						info.canPublish = true;
						info.service = service;
						info.serviceName = serviceName;
					} catch (Exception e) {
						DevAssistant.errorln(e);
						log.error(e);
					}
				}
				return info;
			}
			
		}.start();
	}
}