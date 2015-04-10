// com.antelope.ci.bus.engine.access.storage.BusEngineStorageComplexActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.storage;

import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusComplexActivator;
import com.antelope.ci.bus.osgi.ServicePublishHook;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月9日		上午10:21:41 
 */
public class BusEngineStorageComplexActivator extends BusComplexActivator {

	@Override
	protected void customInit() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void run() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void destroy() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleLoadService(String clsName, ServiceReference ref,
			Object service) throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleUnloadService(ServiceReference ref)
			throws CIBusException {
		
		
	}

	@Override
	protected void handleStopAllService() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addServices() throws CIBusException {
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
		
		// TODO Auto-generated method stub
		
	}

}
