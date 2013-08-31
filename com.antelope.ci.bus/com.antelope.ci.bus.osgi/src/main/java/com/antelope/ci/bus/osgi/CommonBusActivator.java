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
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.configration.ResourceReader;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 为CI BUS定义bunlde activator
 * CI BUS所有的bundle的activator必须由此类来实现
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-29		下午3:17:02 
 */
public abstract class CommonBusActivator implements BundleActivator, ServiceListener {
	private static final String PACKET_SUFFIX = "com.antelope.ci.bus";
	private static final String PACKET_SERVICE = "service";
	private static final String PROPS_FILE = "bus";
	private static final String BUS_LOAD_SERVICES = "bus.load.services";
	private static final String DIVISION = ",";
	protected BundleContext m_context;
	protected static Map<String, ServiceReference> serviceMap = new HashMap<String, ServiceReference>();
	protected static Properties properties;				// bundle的属性
	private List<String> loadServices = new ArrayList<String>();				// 需要加载的service列表
	
	public CommonBusActivator() {
		
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
			BasicConfigrationReader reader = new ResourceReader();
			reader.addResource(props_url.getFile());
			properties = reader.getProps();
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
		unloadService();				// 卸载service
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
	        if (event.getType() == ServiceEvent.REGISTERED) {
	        	loadService();
	        }  else if (event.getType() == ServiceEvent.UNREGISTERING) {
	        	unloadService();
	        } else if (event.getType() == ServiceEvent.MODIFIED) {
	    		unloadService();
	    		loadService();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/*
	 * 加载osgi注册的service
	 */
	private void loadService() throws CIBusException, InvalidSyntaxException {
		String clazz = null;
		String filter = null;
		for (ServiceReference ref : m_context.getServiceReferences(clazz, filter)) {
			String cls_name = ref.getClass().getName();
			if (isLoad(cls_name))
				serviceMap.put(cls_name, ref);
		}
		handleLoadService();
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
	 * 卸载注册的service
	 */
	private void unloadService() throws CIBusException {
		for (ServiceReference ref : serviceMap.values()) {
			m_context.ungetService(ref);
		}
		handleUnloadService();
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
	protected abstract void handleUnloadService() throws CIBusException;
	
	/*
	 * 增加osgi service
	 */
	protected abstract void addServices() throws CIBusException;
	
	/*
	 * 清除osgi service
	 */
	protected abstract void removeServices() throws CIBusException;
}

