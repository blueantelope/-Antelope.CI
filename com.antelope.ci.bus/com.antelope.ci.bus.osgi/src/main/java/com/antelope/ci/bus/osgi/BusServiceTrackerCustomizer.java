// com.antelope.ci.bus.osgi.BusServiceTrackerCustomizer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.osgi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-6-23		下午5:59:27 
 */
public class BusServiceTrackerCustomizer implements ServiceTrackerCustomizer {
	private final static BusServiceTrackerCustomizer serviceTraker = new BusServiceTrackerCustomizer();
	public final static BusServiceTrackerCustomizer getServiceTraker() {
		return serviceTraker;
	}
	
	private final static ReadWriteLock service_lock = new ReentrantReadWriteLock();
	private final static Lock service_readLock = service_lock.readLock();
	private final static Lock service_writeLock = service_lock.writeLock();
	private boolean provideLogService;
	private static final String LOG_SERVICE_NAME = "com.antelope.ci.bus.logger.service.BusLogService";
	private static ServiceReference log_ref = null;
	private static Object logService = null;
	private static Map<String, List<BusServiceInfo>> serviceMap;
	private BundleContext context;
	private BusServiceTrackerCustomizer() {
		serviceMap = new ConcurrentHashMap<String, List<BusServiceInfo>>();
	}
	
	public void setContext(BundleContext context) {
		this.context = context;
	}
	
	public void setProvideLogService(boolean provideLogService) {
		this.provideLogService = provideLogService;
	}

	@Override public Object addingService(ServiceReference reference) {
		try {
			return loadService(reference);
		} catch (CIBusException e) {
			DevAssistant.assert_exception(e);
			return null;
		}
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
		// nothing to do
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		try {
			String service_name = (String) reference.getProperty(BusOsgiConstant.SERVICE_NAME);
			String service_class_name = (String) reference.getProperty(BusOsgiConstant.SERVICE_CLASS_NAME);
			unloadService(service_name, service_class_name,reference, service);
		} catch (CIBusException e) {
			DevAssistant.assert_exception(e);
		}
	}
	
	private Object loadService(ServiceReference ref) throws CIBusException {
		Object service = context.getService(ref);
		String service_name = (String) ref.getProperty(BusOsgiConstant.SERVICE_NAME);
		String service_class_name = (String) ref.getProperty(BusOsgiConstant.SERVICE_CLASS_NAME);
		if (service_name.equals(LOG_SERVICE_NAME)) {
			if (!provideLogService)
				loadLogService(ref, service);
		} else {
			service_writeLock.lock();
			try {
				if (serviceMap.get(service_name) == null) {
					serviceMap.put(service_name, new ArrayList<BusServiceInfo>());
				}
				List<BusServiceInfo> infoList = serviceMap.get(service_name);
				BusServiceInfo info = new BusServiceInfo(service_class_name, service, ref);
				infoList.add(info);
			} catch (Exception e) {
				
			} finally {
				service_writeLock.unlock();
			}
		}
		DevAssistant.assert_out("service类：" + service.getClass().getName() +", 调用者：" + this.getClass().getName());
		return service;
	}
	
	/*
	 * 加载日志service
	 */
	private void loadLogService(ServiceReference ref, Object service) {
		if (log_ref == null) {
			log_ref = ref;
		}
		if (logService == null) {
			logService = service;
		}
		DevAssistant.assert_out("logService : " + logService);
		if (serviceMap.get(LOG_SERVICE_NAME) == null) {
			DevAssistant.assert_out("加载日志service");
			service_writeLock.lock();
			List<BusServiceInfo> logServiceList = new ArrayList<BusServiceInfo>();
			String service_name = (String) log_ref.getProperty(BusOsgiConstant.SERVICE_NAME);
			BusServiceInfo logInfo = new BusServiceInfo(service_name, logService, log_ref);
			logServiceList.add(logInfo);
			serviceMap.put(LOG_SERVICE_NAME, logServiceList);
			service_writeLock.unlock();
		}
	}
	
	/*
	 * 卸载osgi注册的service
	 */
	private void unloadService(String service_name, String servcie_class_name, ServiceReference ref, Object service) throws CIBusException {
		context.ungetService(ref);
		ref = null;
		service = null;
		if (serviceMap.get(service_name) != null) {
			service_writeLock.lock();
			try {
				int del_index = -1;
				for (BusServiceInfo info : serviceMap.get(service_name)) {
					if (info.equals(servcie_class_name)) {
						break;
					}
					del_index++;
				}
				if (del_index != -1) {
					serviceMap.get(service_name).remove(del_index);
				}
				if (serviceMap.get(service_name).isEmpty())
					serviceMap.remove(service_name);
			} catch (Exception e) {
				
			} finally {
				service_writeLock.unlock();
			}
		}
	}
}  

