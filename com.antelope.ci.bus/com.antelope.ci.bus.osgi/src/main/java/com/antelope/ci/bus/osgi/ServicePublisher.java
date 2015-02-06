// com.antelope.ci.bus.osgi.ServicePublisher.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.osgi;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.DevAssistant;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月6日		下午2:32:23 
 */
public class ServicePublisher {
	private static final Logger log = Logger.getLogger(ServicePublisher.class);
	
	public static void publish(BundleContext m_context, String scan_path) {
		new ServicePublishHook(m_context, scan_path) {
			@Override protected ServicePublishInfo fetchService(Class clazz) {
				ServicePublishInfo info = new ServicePublishInfo();
				if (IService.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(Service.class)) {
					try {
						Service service =  (Service) clazz.getAnnotation(Service.class);
						Object serviceObj = clazz.newInstance();
						String serviceName = service.name();
						info.canPublish = true;
						info.service = serviceObj;
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

