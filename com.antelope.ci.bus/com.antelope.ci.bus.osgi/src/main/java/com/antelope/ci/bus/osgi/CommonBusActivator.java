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
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.DebugUtil;
import com.antelope.ci.bus.common.PropertiesUtil;
import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.configration.URLResourceReader;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 为CI BUS定义bunlde activator
 * CI BUS所有的bundle的activator必须由此类来实现
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-29		下午3:17:02 
 */
public abstract class CommonBusActivator implements BundleActivator, ServiceListener {
	private static final String LOGSERVICE_CLSNAME = "com.antelope.ci.bus.logger.service.BusLogService";
	private static final String PACKET_SUFFIX = "com.antelope.ci.bus";
	private static final String PACKET_SERVICE = "service";
	private static final String PROPS_FILE = "/META-INF/bus.properties";
	private static final String BUS_LOAD_SERVICES = "bus.load.services";
	private static final String DIVISION = ",";
	protected BundleContext m_context;
	protected static Map<String, ServiceReference> serviceMap = new HashMap<String, ServiceReference>();
	protected static Properties properties;													// bundle的属性
	protected List<String> loadServices = new ArrayList<String>();				// 需要加载的service列表
	protected static ServiceReference log_ref = null;
	protected static Object logService = null;
	
	
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
	
	public static Map<String, ServiceReference> getServiceMap() {
		return serviceMap;
	}
	
	public static ServiceReference getService(String clazz) {
		return serviceMap.get(clazz);
	}
	
	protected URL getResource(String name) {
		if (m_context != null)
			return m_context.getBundle().getResource("/" + name);
		
		return null;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		m_context = context;
		init();														// 初始化
		addServices();										// 增加service
		context.addServiceListener(this);			// 监听service
		run();													// 自定义运行
	}
	
	/*
	 * 初始化
	 * 加载bus.propertis
	 * 得到加载service列表
	 */
	protected void init() throws CIBusException {
		loadProps();
		initLoadServices();
	}
	
	/*
	 * 加载bundle默认配置文件bus.properties
	 */
	private void loadProps() throws CIBusException {
		URL props_url = m_context.getBundle().getResource(PROPS_FILE);
		if (props_url != null) {
			DebugUtil.assert_out("bus.properties url为" + props_url);
			BasicConfigrationReader reader = new URLResourceReader();
			reader.addResource(props_url.toString());
			properties.putAll(reader.getProps());
		}
	}
	
	/*
	 * 初始化service列表
	 * 由bus.properties中的bus.load.services一项中得到
	 */
	private void initLoadServices() {
		if (properties != null) {
			String load_services = properties.getProperty(BUS_LOAD_SERVICES);
			if (load_services != null) {
				for (String load_service : load_services.split(DIVISION)) {
					loadServices.add(load_service.trim().toLowerCase());
				}
			}
		}
	}

	
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		removeServices();
		stopAllService();				// 停止所有service
		destroy();						// 自定义其它停止操作
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.ServiceEvent)
	 */
	@Override
	public void serviceChanged(ServiceEvent event)  {
		try {
			ServiceReference ref = event.getServiceReference();
	        if (event.getType() == ServiceEvent.REGISTERED) {
	        		loadService(ref);
	        }  else if (event.getType() == ServiceEvent.UNREGISTERING) {
	        		unloadService(ref);
	        } else if (event.getType() == ServiceEvent.MODIFIED) {
		    		unloadService(ref);
		    		loadService(ref);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/*
	 * 加载osgi注册的service
	 */
	private void loadService(ServiceReference ref) throws CIBusException {
		String cls_name = ref.getClass().getName();
		if (LOGSERVICE_CLSNAME.equals(cls_name)) {
			loadLogService();
		} else if (isLoad(cls_name)) {
			serviceMap.put(cls_name, ref);
		}
		handleLoadService();
	}
	
	/*
	 * 卸载osgi注册的service
	 */
	private void unloadService(ServiceReference ref) throws CIBusException {
		String cls_name = ref.getClass().getName();
		if (LOGSERVICE_CLSNAME.equals(cls_name)) {
			if (!unloadLogService(ref))
				m_context.ungetService(ref);
		} else if (isLoad(cls_name)) {
			m_context.ungetService(ref);
			serviceMap.remove(cls_name);
		}
		handleUnloadService(ref);
	}
	
	/*
	 * 加载日志service
	 */
	private void loadLogService() {
		if (log_ref == null && !this.getClass().getName().contains("com.antelope.ci.bus.log")) {
			DebugUtil.assert_out("加载日志service");
			log_ref = serviceMap.get(LOGSERVICE_CLSNAME);
			if (log_ref != null)
				logService = log_ref;
		}
	}
	
	/*
	 * 卸载日志service
	 */
	private boolean unloadLogService(ServiceReference ref) {
		if (log_ref != null && LOGSERVICE_CLSNAME.equals(ref.getClass().getName()) && logService != null) {
			m_context.ungetService(log_ref);
			log_ref = null;
			logService = null;
			return true;
		}
		return false;
	}
	
	/*
	 * 是否加载service
	 * 加载的service为CI BUS定义并且在加载列表中
	 */
	private boolean isLoad(String clsName) {
		if (clsName.startsWith(PACKET_SUFFIX) && clsName.contains(PACKET_SERVICE)) {
			for (String loadService : loadServices) {
				if (loadService.equals(clsName))
					return true;
			}
		}
		
		return false;
	}
	
	/*
	 * 停止注册的service
	 */
	private void stopAllService() throws CIBusException {
		for (ServiceReference ref : serviceMap.values()) {
			if (unloadLogService(ref))		// 卸载日志service
				continue;
			m_context.ungetService(ref);
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
	 * @param  
	 * @return void
	 * @throws
	 */
	protected abstract void handleLoadService() throws CIBusException; 
	
	/**
	 * 处理卸载service后的操作
	 * @param  
	 * @return void
	 * @throws
	 */
	protected abstract void handleUnloadService(ServiceReference ref) throws CIBusException;
	
	/**
	 * 处理停止所有serivice后的操作
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void handleStopAllService() throws CIBusException;
	
	/**
	 * 增加osgi service
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void addServices() throws CIBusException;
	
	/**
	 * 清除osgi service
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	protected abstract void removeServices() throws CIBusException;
}

