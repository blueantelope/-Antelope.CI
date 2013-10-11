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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.antelope.ci.bus.common.DebugUtil;
import com.antelope.ci.bus.common.PropertiesUtil;
import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.configration.URLResourceReader;
import com.antelope.ci.bus.common.exception.CIBusException;

/**
 * 为CI BUS定义bunlde activator CI BUS所有的bundle的activator必须由此类来实现
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-8-29 下午3:17:02
 */
public abstract class CommonBusActivator implements BundleActivator {
	protected static final String LOGSERVICE_CLSNAME = "com.antelope.ci.bus.logger.service.BusLogService";
	private static final String PACKET_SUFFIX = "com.antelope.ci.bus";
	private static final String PACKET_SERVICE = "service";
	private static final String PROPS_FILE = "/META-INF/bus.properties";
	private static final String BUS_LOAD_SERVICES = "bus.load.services";
	private static final String DIVISION = ",";
	protected BundleContext m_context;
	protected static Map<String, ServiceInfo> serviceMap = new HashMap<String, ServiceInfo>();
	protected static Properties properties; // bundle的属性
	protected List<String> loadServices = new ArrayList<String>(); // 需要加载的service列表
	protected static ServiceReference log_ref = null;
	protected static Object logService = null;
	private List<ServiceTracker> trackerList = new ArrayList<ServiceTracker>();
	protected boolean logServiceProvider = false;

	public CommonBusActivator() {
		super();
		properties = new Properties();
	}

	public CommonBusActivator(Properties props) {
		properties = props;
	}

	public static Properties getProperties() {
		return properties;
	}

	public static Map<String, ServiceInfo> getServiceMap() {
		return serviceMap;
	}

	public static ServiceReference getServiceReference(String clazz) {
		return serviceMap.get(clazz).ref;
	}

	public static Object getService(String clazz) {
		return serviceMap.get(clazz).service;
	}

	protected URL getResource(String name) {
		if (m_context != null)
			return m_context.getBundle().getResource("/" + name);

		return null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		m_context = context;
		init(); // 初始化
		loadServicesByTrack();
		addServices(); // 增加service
		run(); // 自定义运行
	}

	/*
	 * 初始化 加载bus.propertis 得到加载service列表
	 */
	protected void init() throws CIBusException {
		loadProps();
		initDefaultService();
		initLoadServices();
		customInit();
	}

	/*
	 * 加载bundle默认配置文件bus.properties
	 */
	private void loadProps() throws CIBusException {
		URL props_url = m_context.getBundle().getResource(PROPS_FILE);
		if (props_url != null) {
			BasicConfigrationReader reader = new URLResourceReader();
			reader.addResource(props_url.toString());
			properties.putAll(reader.getProps());
		}
	}

	/*
	 * 初始化默认的service列表
	 */
	private void initDefaultService() {
		// log service
		loadServices.add(LOGSERVICE_CLSNAME);
	}
	
	/*
	 * 初始化service列表 由bus.properties中的bus.load.services一项中得到
	 */
	private void initLoadServices() {
		if (properties != null) {
			String load_services = properties.getProperty(BUS_LOAD_SERVICES);
			if (load_services != null) {
				for (String load_service : load_services.split(DIVISION)) {
					loadServices.add(load_service.trim());
				}
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
	private void loadServicesByTrack() throws CIBusException {
		for (String  cls_name : loadServices) {
			ServiceTracker tracker = new ServiceTracker(m_context, cls_name, new BusServiceTrackerCustomizer(cls_name));  
			tracker.open();
			trackerList.add(tracker);
		}
	}
	
	private class BusServiceTrackerCustomizer implements ServiceTrackerCustomizer {
		private String clsName;
		
		private BusServiceTrackerCustomizer(String clsName) {
			this.clsName = clsName;
		}

		@Override
		public Object addingService(ServiceReference reference) {
			try {
				return loadService(reference, clsName);
			} catch (CIBusException e) {
				DebugUtil.assert_exception(e);
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
				unloadService(clsName, reference, service);
			} catch (CIBusException e) {
				DebugUtil.assert_exception(e);
			}
		}  
        
	}  

	/*
	 * 加载osgi注册的service
	 */
	private Object loadService(ServiceReference ref, String clsName) throws CIBusException {
		Object service = m_context.getService(ref);
		String cls_name = service.getClass().getName();
		if (cls_name.equals(LOGSERVICE_CLSNAME)) {
			if (!logServiceProvider)
				loadLogService(ref, service);
		} else {
			serviceMap.put(cls_name, new ServiceInfo(service, ref));
		}
		handleLoadService(clsName, ref, service);
		DebugUtil.assert_out("service类：" + service.getClass().getName() +", 调用者：" + this.getClass().getName());
		return service;
	}

	/*
	 * 卸载osgi注册的service
	 */
	private void unloadService(String cls_name, ServiceReference ref, Object service) throws CIBusException {
		m_context.ungetService(ref);
		ref = null;
		service = null;
		serviceMap.remove(cls_name);
		handleUnloadService(ref);
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
		DebugUtil.assert_out("logService : " + logService);
		if (serviceMap.get(LOGSERVICE_CLSNAME) == null) {
			DebugUtil.assert_out("加载日志service");
			serviceMap.put(LOGSERVICE_CLSNAME, new ServiceInfo(logService, log_ref));
		}
	}

	/*
	 * 停止注册的service
	 */
	private void stopAllService() throws CIBusException {
		for (String cls_name : serviceMap.keySet()) {
			ServiceInfo si = serviceMap.get(cls_name);
			if (cls_name.equals(LOGSERVICE_CLSNAME)) {
				log_ref = null;
				logService = null;
			}
			unloadService(cls_name, si.ref, si.service);
		}
		for (ServiceTracker tracker : trackerList) {
			tracker.close();
			tracker = null;
		}
		handleStopAllService();
		serviceMap = null;
	}

	/*
	 * 取得配置文件中的整形数参数
	 */
	protected int getIntProp(String key, int default_prop) {
		return PropertiesUtil.getInt(properties, key, default_prop);
	}

	/*
	 * 取得配置文件中的字符串参数
	 */
	protected String getStringProp(String key, String default_prop) {
		return PropertiesUtil.getString(properties, key, default_prop);
	}

	/*
	 * 取得配置文件中的布尔型参数
	 */
	protected boolean getBooleanProp(String key, boolean default_prop) {
		return PropertiesUtil.getBoolean(properties, key, default_prop);
	}
	
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
	protected abstract void handleUnloadService(ServiceReference ref)
			throws CIBusException;

	/**
	 * 处理停止所有serivice后的操作
	 * 
	 * @param @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void handleStopAllService() throws CIBusException;

	/**
	 * 增加osgi service
	 * 
	 * @param @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void addServices() throws CIBusException;

	/**
	 * 清除osgi service
	 * 
	 * @param @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void removeServices() throws CIBusException;

	// osgi service信息
	private static class ServiceInfo {
		public Object service;
		public ServiceReference ref;

		public ServiceInfo(Object service, ServiceReference ref) {
			super();
			this.service = service;
			this.ref = ref;
		}

		public Object getService() {
			return service;
		}

		public ServiceReference getRef() {
			return ref;
		}
	}
}
