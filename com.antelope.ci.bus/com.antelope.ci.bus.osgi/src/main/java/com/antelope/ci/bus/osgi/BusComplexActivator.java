// com.antelope.ci.bus.framework.AbstractBundleActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.osgi;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;

/**
 * 为CI BUS定义bunlde activator CI BUS所有的bundle的activator必须由此类来实现
 * @author blueantelope
 * @version 0.1
 * @Date 2013-8-29 下午3:17:02
 */
public abstract class BusComplexActivator implements BundleActivator {
	protected BundleContext bundle_context;
	protected BusActivatorContext bus_context;
	protected String bus_context_clazz;

	public BusComplexActivator() {
		super();
		construct();
	}
	
	public BusComplexActivator(String bus_context_clazz) {
		super();
		this.bus_context_clazz = bus_context_clazz;
		construct();
	}
	
	private void construct() {
		System.out.println(">>>>>> start bus activator: " + this.getClass().getName() + " <<<<<<");
		if (StringUtil.empty(bus_context_clazz))
			this.bus_context_clazz = BusActivatorContext.class.getName();
	}


	protected URL getResource(String name) {
		if (bundle_context != null)
			return bundle_context.getBundle().getResource("/" + name);

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
		bundle_context = context;
		loadBusContext();
		init(); // 初始化
		loadServices();
		addServices(); // 增加service
		run(); // 自定义运行
	}
	
	protected void loadBusContext() throws CIBusException {
		ClassLoader bundle_classloader = classLoader();
		BusActivatorContextProxy contextProxy = (BusActivatorContextProxy) ProxyUtil.invokeStaticRet(
				bundle_classloader, BusActivatorContextProxy.class.getName(), "getContextProxy");
		bus_context = (BusActivatorContext) ProxyUtil.newObject(bus_context_clazz, bundle_classloader);
		bus_context.setContext(bundle_context);
		if (BusActivatorContext.class.isAssignableFrom(bus_context.getClass())) {
			contextProxy.initContext(bus_context);
		} else {
			throw new CIBusException("", "Error, Create Activator Context Object ");
		}
	}

	/*
	 * 初始化 加载bus.propertis 得到加载service列表
	 */
	protected void init() throws CIBusException {
		loadEnv();
		initDefaultServices();
		initLoadServices();
		customInit();
	}

	/*
	 * 加载环境配置
	 */
	private void loadEnv() throws CIBusException {
		bus_context.loadBusProperties(); // 加载bus.properties
	}

	/*
	 * 初始化默认的service列表
	 */
	private void initDefaultServices() {
		// log service
		bus_context.addLogService();
	}
	
	/*
	 * 初始化service列表 由bus.properties中的bus.load.services一项中得到
	 */
	private void initLoadServices() {
		String load_services = bus_context.getLoadServiceProps();
		if (load_services != null) {
			for (String load_service : load_services.split(BusActivatorContext.DIVISION))
				bus_context.addLoadService(load_service.trim());
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
		for (String  service : bus_context.getServiceList()) {
			try {
				Filter filter = bundle_context.createFilter("(objectClass=" + service + ")");
				ServiceTracker tracker = new ServiceTracker(bundle_context, filter, new BusServiceTrackerCustomizer());  
				tracker.open();
				bus_context.addTracker(tracker);
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
				unloadService(service_name, service_class_name, reference);
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
		if (service_name.equals(BusActivatorContext.LOG_SERVICE_NAME)) {
			bus_context.loadLogService(ref, service);
		} else {
			bus_context.putService(ref);
		}
		handleLoadService(service_class_name, ref, service);
		DevAssistant.assert_out("service类：" + service.getClass().getName() +", 调用者：" + this.getClass().getName());
		return service;
	}

	/*
	 * 卸载osgi注册的service
	 */
	protected void unloadService(String service_name, String servcie_class_name, ServiceReference ref) throws CIBusException {
		bundle_context.ungetService(ref);
		bus_context.removeService(service_name, servcie_class_name);
		handleUnloadService(ref);
	}

	/*
	 * 停止注册的service
	 */
	protected void stopAllService() throws CIBusException {
		Map<String, List<BusServiceInfo>> serviceMap = bus_context.getServiceMap();
		for (String service_name : serviceMap.keySet()) {
			if (serviceMap.get(service_name) != null) {
				for (BusServiceInfo info : serviceMap.get(service_name)) {
					bus_context.unloadLogService(service_name);
					ServiceReference ref = info.ref;
					Object service = info.service;
					String service_class_name = (String) ref.getProperty(BusOsgiConstant.SERVICE_CLASS_NAME);
					unloadService(service_name, service_class_name, ref);
				}
			}
		}
		bus_context.clearTracker();
		handleStopAllService();
		bus_context.clearService();
	}
	
	protected Object fetchService(String serviceName) {
		int n = 0;
		while (n < BusActivatorContext.FETCH_SERVICE_TIMES) {
			Object service = bus_context.getUsingService(serviceName);
			if (service != null)
				return service;
			n++;
			try {
				Thread.sleep(BusActivatorContext.FETCH_SERVICE_SLEEP);
			} catch (InterruptedException e) {}
		}
		
		return null;
	}
	
	protected ClassLoader classLoader() {
		return BusOsgiUtil.getBundleClassLoader(bundle_context);
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
	 * @param
	 * @return void
	 * @throws
	 */
	protected abstract void handleUnloadService(ServiceReference ref) throws CIBusException;

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
	 * @param @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void addServices() throws CIBusException;

	/**
	 * 清除osgi service
	 * @param @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void removeServices() throws CIBusException;
}
