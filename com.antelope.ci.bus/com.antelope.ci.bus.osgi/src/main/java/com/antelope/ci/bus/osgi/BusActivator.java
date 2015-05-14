// com.antelope.ci.bus.framework.AbstractBundleActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.osgi;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;

/**
 * 为CI BUS定义bunlde activator CI BUS所有的bundle的activator必须由此类来实现
 * @author blueantelope
 * @version 0.1
 * @Date 2013-8-29 下午3:17:02
 * @Deprecated replace by {@link #com.antelope.ci.bus.osgi.BusComplexActivator}
 */
@Deprecated
public abstract class BusActivator implements BundleActivator {
	protected static final int FETCH_SERVICE_TIMES = 3;
	protected static final long FETCH_SERVICE_SLEEP = 500;
	protected static Logger log4j = null; // log4j
	protected static final String LOG_SERVICE_NAME = "com.antelope.ci.bus.logger.service.BusLogService";
	private static final String PACKET_SUFFIX = "com.antelope.ci.bus";
	private static final String PACKET_SERVICE = "service";
	private static final String BUS_LOAD_SERVICES = "bus.load.services";
	private static final String DIVISION = ",";
	protected static BundleContext bundle_context;
	protected static BusPropertiesHelper propsHelper;
	protected static Map<String, List<BusServiceInfo>> serviceMap;
	protected static Properties properties; // bundle的属性
	protected static List<String> serviceList; // 需要加载的service列表
	protected static ServiceReference log_ref = null;
	protected static Object logService = null;
	private List<ServiceTracker> trackerList;
	protected boolean logServiceProvider = false;

	public BusActivator() {
		super();
		System.out.println(">>>>>> start bus activator: " + this.getClass().getName() + " <<<<<<");
		serviceMap = new ConcurrentHashMap<String, List<BusServiceInfo>>();
		properties = new Properties();
		serviceList = new Vector<String>();
		trackerList = new ArrayList<ServiceTracker>();
	}
	
	public BusActivator(Properties props) {
		properties = props;
	}

	public static void addLoadService(String serviceName) {
		boolean loaded = false;
		for (String service : serviceList) {
			if (service.equals(serviceName)) {
				loaded = true;
				break;
			}
		}
		if (!loaded)
			serviceList.add(serviceName);
	}
	
	public static Properties getActivatorProps() {
		return properties;
	}
	
	public static BundleContext getContext() {
		return bundle_context;
	}
	
	public static ClassLoader getClassLoader() {
		return BusOsgiUtil.getBundleClassLoader(bundle_context);
	}

	public static Map<String, List<BusServiceInfo>> getServiceMap() {
		return serviceMap;
	}
	
	public static void mapService(String serviceName, BusServiceInfo serviceInfo) {
		if (getService(serviceName, serviceInfo.getClassName()) != null)
			return;
		List<BusServiceInfo> serviceList = serviceMap.get(serviceName);
		if (serviceList == null) {
			serviceList = new ArrayList<BusServiceInfo>();
			serviceMap.put(serviceName, serviceList);
		}
		serviceList.add(serviceInfo);
	}
	
	public static ServiceReference getServiceReference(String serviceName, String className) {
		BusServiceInfo info = getBusServiceInfo(serviceName, className);
		if (info != null) return info.ref;
		return null;
	}

	public static Object getService(String serviceName, String className) {
		BusServiceInfo info = getBusServiceInfo(serviceName, className);
		if (info != null) return info.service;
		return null;
	}
	
	public static Object getUsingService(String serviceName) {
		List<BusServiceInfo> infoList =  serviceMap.get(serviceName);
		if (infoList != null && !infoList.isEmpty())
			return infoList.get(0).getService();
		return null;
	}
	
	public static List<Object> getServices(String serviceName) {
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
	
	private static BusServiceInfo getBusServiceInfo(String serviceName, String className) {
		List<BusServiceInfo> infoList =  serviceMap.get(serviceName);
		if (infoList != null) {
			for (BusServiceInfo info : infoList) {
				if (info.className.equals(className))
					return info;
			}
		}
		return null;
	}

	protected URL getResource(String name) {
		if (bundle_context != null)
			return bundle_context.getBundle().getResource("/" + name);

		return null;
	}
	
	protected Logger log4j() throws CIBusException {
		if (log4j != null)
			return log4j;
		if (logService != null) {
			return instanceLog4j(this.getClass());
		}
		throw new CIBusException("logService is null");
	}
	
	public static Logger getLog4j(Class clazz) throws CIBusException {
		if (logService != null) {
			return instanceLog4j(clazz);
		}
		
		throw new CIBusException("logService is null");
	}
	
	private static Logger instanceLog4j(Class clazz) throws CIBusException{
		Object o = ProxyUtil.invokeRet(logService, "getLog4j", new Object[]{clazz});
		if (o != null) {
			log4j = (Logger) o;
			log4j.info(clazz.getName() + "得到Bus Log Service");
			return log4j;
		}
		throw new CIBusException("could not initialize log4j");
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		bundle_context = context;
		init(); // 初始化
		loadServices();
		publishServices(); // 发布service
		run(); // 自定义运行
	}

	/*
	 * 初始化 加载bus.propertis 得到加载service列表
	 */
	protected void init() throws CIBusException {
		loadProps();
		initDefaultService();
		initLoadServices();
		String[] services = customLoadServices();
		if (services != null)
			serviceList.addAll(Arrays.asList(services));
		customInit();
	}

	/*
	 * 加载bundle默认配置文件bus.properties
	 */
	private void loadProps() throws CIBusException {
		propsHelper = new BusPropertiesHelper(bundle_context);
		properties.putAll(propsHelper.getAll());
	}

	/*
	 * 初始化默认的service列表
	 */
	private void initDefaultService() {
		// log service
		serviceList.add(LOG_SERVICE_NAME);
	}
	
	/*
	 * 初始化service列表 由bus.properties中的bus.load.services一项中得到
	 */
	protected void initLoadServices() {
		if (properties != null) {
			String load_services = properties.getProperty(BUS_LOAD_SERVICES);
			if (load_services != null) {
				for (String load_service : load_services.split(DIVISION))
					serviceList.add(load_service.trim());
			}
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		removeServices();
		stopAllService(); // 停止所有service
		destroy(); // 自定义其它停止操作
	}

	/*
	 * 使用serviceTrack加载所有定义的service
	 */
	private void loadServices() throws CIBusException {
		for (String service : serviceList) {
			try {
				Filter filter = bundle_context.createFilter("(objectClass=" + service + ")");
				ServiceTracker tracker = new ServiceTracker(bundle_context, filter, new BusServiceTrackerCustomizer());  
				tracker.open();
				trackerList.add(tracker);
			} catch (InvalidSyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class BusServiceTrackerCustomizer implements ServiceTrackerCustomizer {
		
		private BusServiceTrackerCustomizer() {
		}

		@Override
		public Object addingService(ServiceReference reference) {
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
				unloadService(service_name, service_class_name, reference, service);
			} catch (CIBusException e) {
				DevAssistant.assert_exception(e);
			}
		}  
	}  

	/*
	 * 加载osgi注册的service
	 */
	private Object loadService(ServiceReference ref) throws CIBusException {
		Object service = bundle_context.getService(ref);
		String service_name = (String) ref.getProperty(BusOsgiConstant.SERVICE_NAME);
		String service_class_name = (String) ref.getProperty(BusOsgiConstant.SERVICE_CLASS_NAME);
		if (service_name.equals(LOG_SERVICE_NAME)) {
			if (!logServiceProvider) {
				loadLogService(ref, service);
				logServiceProvider = true;
			}
		} else {
			BusServiceInfo info = new BusServiceInfo(service_class_name, service, ref);
			if (serviceMap.get(service_name) == null)
				serviceMap.put(service_name, new ArrayList<BusServiceInfo>());
			List<BusServiceInfo> infoList = serviceMap.get(service_name);
			infoList.add(info);
		}
		handleLoadService(service_class_name, ref, service);
		DevAssistant.assert_out("service类：" + service.getClass().getName() +", 调用者：" + this.getClass().getName());
		return service;
	}

	/*
	 * 卸载osgi注册的service
	 */
	protected void unloadService(String service_name, String servcie_class_name, ServiceReference ref, Object service) throws CIBusException {
		bundle_context.ungetService(ref);
		ref = null;
		service = null;
		if (serviceMap.get(service_name) != null) {
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
		}
		handleUnloadService(ref);
	}

	/*
	 * 加载日志service
	 */
	private void loadLogService(ServiceReference ref, Object service) {
		if (log_ref == null)
			log_ref = ref;
		if (logService == null)
			logService = service;
		DevAssistant.assert_out("logService : " + logService);
		String service_name = (String) log_ref.getProperty(BusOsgiConstant.SERVICE_NAME);
		BusServiceInfo log_service_info = new BusServiceInfo(service_name, logService, log_ref);
		mapService(LOG_SERVICE_NAME, log_service_info);
	}

	/*
	 * 停止注册的service
	 */
	private void stopAllService() throws CIBusException {
		for (String service_name : serviceMap.keySet()) {
			if (serviceMap.get(service_name) != null) {
				for (BusServiceInfo info : serviceMap.get(service_name)) {
					if (service_name.equals(LOG_SERVICE_NAME)) {
						log_ref = null;
						logService = null;
					}
					ServiceReference ref = info.ref;
					Object service = info.service;
					String service_class_name = (String) ref.getProperty(BusOsgiConstant.SERVICE_CLASS_NAME);
					unloadService(service_name, service_class_name, ref, service);
				}
			}
		}
		for (ServiceTracker tracker : trackerList) {
			tracker.close();
			tracker = null;
		}
		handleStopAllService();
		serviceMap = null;
	}
	
	public static String getBanner() {
		return propsHelper.getBanner();
	}

	protected Object fetchService(String serviceName) {
		int n = 0;
		while (n < FETCH_SERVICE_TIMES) {
			Object service = getUsingService(serviceName);
			if (service != null)
				return service;
			n++;
			try {
				Thread.sleep(FETCH_SERVICE_SLEEP);
			} catch (InterruptedException e) {}
		}
		
		return null;
	}
	
	protected abstract String[] customLoadServices();
	
	/**
	 * 各个activator自定义的初始化
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void customInit() throws CIBusException;

	/**
	 * 运行bunlde进需要自定义的运行
	 * @param
	 * @return void
	 * @throws
	 */
	protected abstract void run() throws CIBusException;

	/**
	 * bundle卸载时，需要进行的操作
	 * 
	 * @param
	 * @return void
	 * @throws
	 */
	protected abstract void destroy() throws CIBusException;

	/**
	 * 处理加载service后的操作
	 * @param  @param clsName
	 * @param  @param ref
	 * @param  @param service
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void handleLoadService(String clsName, ServiceReference ref, Object service) throws CIBusException;

	/**
	 * 处理卸载service后的操作
	 * 
	 * @param
	 * @return void
	 * @throws
	 */
	protected abstract void handleUnloadService(ServiceReference ref) throws CIBusException;

	/**
	 * 处理停止所有serivice后的操作
	 * @param @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void handleStopAllService() throws CIBusException;

	/**
	 * publish osgi services
	 * @param @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void publishServices() throws CIBusException;

	/**
	 * 清除osgi service
	 * @param @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void removeServices() throws CIBusException;
}
