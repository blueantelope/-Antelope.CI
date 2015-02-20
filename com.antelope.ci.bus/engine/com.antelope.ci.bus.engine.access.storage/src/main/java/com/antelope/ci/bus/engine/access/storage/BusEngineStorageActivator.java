// com.antelope.ci.bus.storage.BusStorageActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.storage;

import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusActivator;
import com.antelope.ci.bus.osgi.ServicePublishHook;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-9		上午11:46:26 
 */
public class BusEngineStorageActivator extends BusActivator {
	@Override
	protected void customInit() throws CIBusException {
		
	}

	@Override
	protected void run() throws CIBusException {
		
	}

	@Override
	protected void destroy() throws CIBusException {
		
	}

	@Override
	protected void handleLoadService(String clsName, ServiceReference ref,
			Object service) throws CIBusException {
		
	}

	@Override
	protected void handleUnloadService(ServiceReference ref)
			throws CIBusException {
		
	}

	@Override
	protected void handleStopAllService() throws CIBusException {
		
	}

	@Override
	protected void publishServices() throws CIBusException {
		new ServicePublishHook(bundle_context, "com.antelope.ci.bus.storage") {
			@Override protected ServicePublishInfo fetchService(Class clazz) {
				ServicePublishInfo info = new ServicePublishInfo();
				if (StorageAcesss.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(StorageService.class)) {
					try {
						StorageService ss =  (StorageService) clazz.getAnnotation(StorageService.class);
						StorageAcesss service = (StorageAcesss) clazz.newInstance();
						String serviceName = ss.serviceName();
						info.canPublish = true;
						info.service = service;
						info.serviceName = serviceName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return info;
			}
			
		}.start();
	}

	@Override
	protected void removeServices() throws CIBusException {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.BusActivator#customLoadServices()
	 */
	@Override
	protected String[] customLoadServices() {
		return null;
	}
}
