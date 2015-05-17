// com.antelope.ci.bus.engine.access.AccessPublisher.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.osgi.BusContext;
import com.antelope.ci.bus.osgi.ServicePublishHook;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月17日		上午11:17:25 
 */
public class AccessUtil {
	private static final Logger log = Logger.getLogger(AccessUtil.class);
	
	public static void publish(BusContext context, String packagePath, final Class... iClass) {
		new ServicePublishHook(context, packagePath) {
			@Override protected ServicePublishInfo fetchService(Class clazz) {
				ServicePublishInfo info = new ServicePublishInfo();
				if (validIAccess(clazz, iClass) && clazz.isAnnotationPresent(Access.class)) {
					try {
						Access access =  (Access) clazz.getAnnotation(Access.class);
						info.canPublish = true;
						info.serviceName = access.name();
						info.service = clazz.newInstance();
					} catch (Exception e) {
						log.error("publish access service failed : " + clazz + "\n" + e);
					}
				}
				return info;
			}
			
		}.start();
	}
	
	private static boolean validIAccess(Class clazz, Class... iClasses) {
		if (iClasses != null) {
			for (Class iClass : iClasses) {
				if (iClass.isAssignableFrom(clazz))
					return true;
			}
		}
		if (IAccess.class.isAssignableFrom(clazz))
			return true;
		
		return false;
	}
}

