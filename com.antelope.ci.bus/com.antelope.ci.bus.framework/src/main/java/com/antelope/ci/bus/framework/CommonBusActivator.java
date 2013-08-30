// com.antelope.ci.bus.framework.AbstractBundleActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 为CI BUS定义bunlde activator
 * CI BUS所有的bundle的activator必须由此类来实现
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-29		下午3:17:02 
 */
public abstract class CommonBusActivator implements BundleActivator, ServiceListener {
	protected static final String PACKET_SUFFIX = "com.antelope.ci.bus";
	protected static final String PACKET_SERVICE = "service";
	protected BundleContext m_context;
	protected static Map<String, ServiceReference> serviceMap = new HashMap<String, ServiceReference>();
	protected static Properties properties;
	
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
		context.addServiceListener(this);			// 监听service
		run();													// 自定义运行
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
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
			if (cls_name.startsWith(PACKET_SUFFIX) && cls_name.contains(PACKET_SERVICE)) {
				serviceMap.put(cls_name, ref);
			}
		}
		handleLoadService();
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
}

