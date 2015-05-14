// com.antelope.ci.bus.osgi.BusActivatorHelper.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.osgi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月11日		下午12:55:00 
 */
public class BusActivatorContext {
	public static final int FETCH_SERVICE_TIMES = 3;
	public static final long FETCH_SERVICE_SLEEP = 500;
	public static final String LOG_SERVICE_NAME = "com.antelope.ci.bus.logger.service.BusLogService";
	public static final String PACKET_SUFFIX = "com.antelope.ci.bus";
	public static final String PACKET_SERVICE = "service";
	public static final String BUS_LOAD_SERVICES = "bus.load.services";
	public static final String DIVISION = ",";
	protected Logger log4j; // log4j
	protected BundleContext bundle_context;
	protected List<ServiceTracker> trackerList;
	protected Map<String, List<BusServiceInfo>> serviceMap;
	protected BusPropertiesHelper bus_props_helper;
	protected Properties properties; // bundle的属性
	protected List<String> loadServiceList; // 需要加载的service列表
	protected boolean logServiceProvider;
	protected ServiceReference log_ref;
	protected Object logService;
	
	public BusActivatorContext() {
		super();
		init();
	}
	
	protected void init() {
		logServiceProvider = false;
		properties = new Properties();
		trackerList = new ArrayList<ServiceTracker>();
		serviceMap = new ConcurrentHashMap<String, List<BusServiceInfo>>();
		loadServiceList = new Vector<String>();
	}
	
	// need not load log service
	public void undesiredLoadLogService() {
		logServiceProvider = true;
	}
	
	public List<String> getServiceList() {
		return loadServiceList;
	}
	
	public void addLogService() {
		addLoadService(LOG_SERVICE_NAME);
	}
	
	public void addLoadService(String serviceName) {
		boolean loaded = false;
		for (String service : loadServiceList) {
			if (service.equals(serviceName)) {
				loaded = true;
				break;
			}
		}
		if (!loaded)
			loadServiceList.add(serviceName);
	}
	
	public Properties getActivatorProps() {
		return properties;
	}
	
	public String getLoadServiceProps() {
		if (properties == null) return null;
		return properties.getProperty(BUS_LOAD_SERVICES);
	}
	
	public void loadBusProperties() throws CIBusException {
		bus_props_helper = new BusPropertiesHelper(bundle_context);
		properties.putAll(bus_props_helper.getAll());
	}
	
	public String getBanner() {
		return bus_props_helper.getBanner();
	}
	
	public void setContext(BundleContext bundle_context) {
		this.bundle_context = bundle_context;
	}
	
	public BundleContext getBundleContext() {
		return bundle_context;
	}
	
	public Logger getLog4j(Class clazz) throws CIBusException {
		if (logService != null) {
			return instanceLog4j(clazz);
		}
		
		throw new CIBusException("logService is null");
	}
	
	private Logger instanceLog4j(Class clazz) throws CIBusException{
		Object o = ProxyUtil.invokeRet(logService, "getLog4j", new Object[]{clazz});
		if (o != null) {
			log4j = (Logger) o;
			log4j.info(clazz.getName() + "得到Bus Log Service");
			return log4j;
		}
		throw new CIBusException("could not initialize log4j");
	}
	
	public ClassLoader getClassLoader() {
		return BusOsgiUtil.getBundleClassLoader(bundle_context);
	}

	public Map<String, List<BusServiceInfo>> getServiceMap() {
		return serviceMap;
	}
	
	public void mapService(String serviceName, BusServiceInfo serviceInfo) {
		if (getService(serviceName, serviceInfo.getClassName()) != null)
			return;
		List<BusServiceInfo> serviceList = serviceMap.get(serviceName);
		if (serviceList == null) {
			serviceList = new ArrayList<BusServiceInfo>();
			serviceMap.put(serviceName, serviceList);
		}
		serviceList.add(serviceInfo);
	}
	
	public Object getService(String serviceName, String className) {
		BusServiceInfo service_info = getBusServiceInfo(serviceName, className);
		if (service_info != null)
			return service_info.service;
		return null;
	}
	
	public ServiceReference getServiceReference(String serviceName, String className) {
		BusServiceInfo info;
		if ((info=getBusServiceInfo(serviceName, className)) != null)
			return info.ref;
		return null;
	}
	
	public List<Object> getServices(String serviceName) {
		List<Object> sList = null;
		List<BusServiceInfo> infoList =  serviceMap.get(serviceName);
		if (infoList != null) {
			sList = new ArrayList<Object>();
			for (BusServiceInfo info : infoList) {
				sList.add(info.getService());
			}
		}
		return sList;
	}
	
	public Object getUsingService(String serviceName) {
		List<BusServiceInfo> service_info_list =  serviceMap.get(serviceName);
		if (service_info_list != null && !service_info_list.isEmpty())
			return service_info_list.get(0).getService();
		return null;
	}
	
	public void putService(ServiceReference ref) {
		Object service = bundle_context.getService(ref);
		String service_name = (String) ref.getProperty(BusOsgiConstant.SERVICE_NAME);
		String service_class_name = (String) ref.getProperty(BusOsgiConstant.SERVICE_CLASS_NAME);
		BusServiceInfo service_info = new BusServiceInfo(service_class_name, service, ref);
		if (serviceMap.get(service_name) == null) {
			serviceMap.put(service_name, new ArrayList<BusServiceInfo>());
		}
		List<BusServiceInfo> infoList = serviceMap.get(service_name);
		infoList.add(service_info);
	}
	
	public void loadLogService(ServiceReference ref, Object service) {
		if (!logServiceProvider) {
			if (log_ref == null)
				log_ref = ref;
			if (logService == null)
				logService = service;
			DevAssistant.assert_out("logService : " + logService);
			String service_name = (String) log_ref.getProperty(BusOsgiConstant.SERVICE_NAME);
			BusServiceInfo log_service_info = new BusServiceInfo(service_name, logService, log_ref);
			mapService(LOG_SERVICE_NAME, log_service_info);
			logServiceProvider = true;
		}
	}
	
	public void unloadLogService(String service_name) {
		if (service_name.equals(LOG_SERVICE_NAME)) {
			log_ref = null;
			logService = null;
		}
	}
	
	public void removeService(String service_name, String servcie_class_name) {
		if (serviceMap.get(service_name) != null) {
			int del_index = -1;
			for (BusServiceInfo service_info : serviceMap.get(service_name)) {
				if (service_info.equals(servcie_class_name)) {
					break;
				}
				del_index++;
			}
			if (del_index != -1) {
				serviceMap.get(service_name).remove(del_index);
			}
			if (serviceMap.get(service_name).isEmpty())
				serviceMap.remove(service_name);
		}
	}
	
	public void clearService() {
		serviceMap.clear();
	}
	
	public void addTracker(ServiceTracker tracker) {
		trackerList.add(tracker);
	}
	
	public void clearTracker() {
		for (ServiceTracker tracker : trackerList) {
			tracker.close();
			tracker = null;
		}
	}
	
	private BusServiceInfo getBusServiceInfo(String serviceName, String className) {
		List<BusServiceInfo> infoList =  serviceMap.get(serviceName);
		if (infoList != null) {
			for (BusServiceInfo info : infoList) {
				if (info.className.equals(className))
					return info;
			}
		}
		return null;
	}
}

