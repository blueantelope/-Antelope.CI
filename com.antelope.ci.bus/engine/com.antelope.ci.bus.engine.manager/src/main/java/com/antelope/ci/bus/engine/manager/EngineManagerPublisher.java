// com.antelope.ci.bus.server.service.ServiceManager.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.PropertiesUtil;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.osgi.BusOsgiUtil;


/**
 * manager对外提供servcie管理
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		下午12:31:45 
 */
public class EngineManagerPublisher {
	private static final Logger log = Logger.getLogger(EngineManagerPublisher.class);
	private static Map<String, BusEngineManager> serviceMap = new ConcurrentHashMap<String, BusEngineManager>();

	public static void publish(BundleContext m_context) {
		new ServicePublishHook(m_context).start();
	}
	
	private static class ServicePublishHook extends Thread {
		private BundleContext m_context;
		private ServicePublishHook(BundleContext m_context) {
			this.m_context = m_context;
		}
		
		public void run() {
			while (true) {
				String cls_name = "";
				try {
					List<String> classList = ClassFinder.findClasspath("com.antelope.ci.bus.engine.manager", 
							BusOsgiUtil.getBundleClassLoader(m_context));
					List<String> regList = new ArrayList<String>();
					for (String cls : classList) {
						cls_name = cls;
						boolean needReg = true;
						for (String service_cls : serviceMap.keySet()) {
							if (cls.equals(service_cls)) {
								needReg = false;
								break;
							}
						}
						if (needReg) {
							Class clazz = ProxyUtil.loadClass(cls, BusOsgiUtil.getBundleClassLoader(m_context));
							if (BusEngineManager.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(EngineManager.class)) {
								EngineManager em =  (EngineManager) clazz.getAnnotation(EngineManager.class);
								BusEngineManager service = (BusEngineManager) clazz.newInstance();
								Properties props = new Properties();
								props.load(service.getClass().getResourceAsStream("manager.properties"));
								String alStr = PropertiesUtil.getString(props, "autoload", "on");
								boolean autoload = false;
								if ("on".equalsIgnoreCase(alStr.trim()))
									autoload = true;
								if (autoload) {
									String serviceName = em.service_name();
									BusOsgiUtil.addServiceToContext(m_context, service, serviceName);
									service.regist(m_context);
									serviceMap.put(cls, service);
									log.info("add engine manager: " + cls_name);
								}
							}
						} else {
							BusEngineManager service = serviceMap.get(cls);
							ManagerParameters parameters = service.getParameters();
							if (parameters.needUnload()) {
								ServiceReference ref = m_context.getServiceReference(cls);
								m_context.ungetService(ref);
								service.unregist(m_context);
								serviceMap.remove(cls);
								log.info("remove engine manager: " + cls_name);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.warn("problem for add engine manager: " + cls_name);
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}