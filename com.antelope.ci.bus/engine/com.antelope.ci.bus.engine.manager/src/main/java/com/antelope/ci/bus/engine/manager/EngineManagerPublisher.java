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
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusOsgiUtil;
import com.antelope.ci.bus.osgi.BusActivator;


/**
 * manager对外提供servcie管理
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		下午12:31:45 
 */
public class EngineManagerPublisher {
	private static final Logger log = Logger.getLogger(EngineManagerPublisher.class);
	private static Map<String, BusEngineManager> managerMap = new ConcurrentHashMap<String, BusEngineManager>();
	private static Map<String, ManagerParameters> parametersMap = new ConcurrentHashMap<String, ManagerParameters>();

	public static void publish(BundleContext m_context, EnginePublishInfo info) {
		new ServicePublishHook(m_context, info).start();
	}
	
	private static class ServicePublishHook extends Thread {
		private BundleContext m_context;
		private EnginePublishInfo info;
		
		private ServicePublishHook(BundleContext m_context, EnginePublishInfo info) {
			this.m_context = m_context;
			this.info = info;
		}
		
		public void run() {
			while (true) {
				if (info.isCheckPart())
					publish(info.getPartClasspath(), 1);
				if (info.isCheckService())
					publish(info.getServiceClasspath(), 2);
				try {
					Thread.sleep(BusEngineManagerActivator.getPublishPeriod());
				} catch (InterruptedException e) { }
			}
		}
		
		private void publish(String classpath, int type) {
			String cls_name = "";
			try {
				List<String> classNameList = ClassFinder.findClasspath(classpath, BusOsgiUtil.getBundleClassLoader(m_context));
				List<String> regList = new ArrayList<String>();
				for (String className : classNameList) {
					if (!isManager(className))
						continue;
					
					Class clazz = ProxyUtil.loadClass(className, BusOsgiUtil.getBundleClassLoader(m_context));
					String serviceName = getServiceName(clazz);
					if (serviceName == null)
						continue;
					ServiceReference serviceReference = BusActivator.getServiceReference(serviceName, className);
					
					if (serviceReference == null && !parametersMap.containsKey(className)) {
						loadManager(clazz, type, true);
						continue;
					}
					
					ManagerParameters parameters = parametersMap.get(className);
					if (parameters == null)
						continue;
					
					BusEngineManager manager = managerMap.get(className);
						switch (parameters.getState()) {
							case 	UNACTIVE:
								if (serviceReference != null) {
									m_context.ungetService(serviceReference);
									manager.unregist(m_context);
									log.info("unactive engine manager: " + cls_name);
								}
								break;
							case ACTIVE:
								if (serviceReference == null) {
									registToContext(manager);
									manager.regist(m_context);
									log.info("active engine manager: " + cls_name);
								}
								break;
							case UNLOAD:
								if (serviceReference != null) {
									m_context.ungetService(serviceReference);
									manager.unregist(m_context);
									managerMap.remove(className);
									log.info("unload engine manager: " + cls_name);
								}
								break;
							case LOAD:
								if (serviceReference == null) {
									loadManager(clazz, type, false);
								break;
						}
						parameters.setState(parameters.getState());
					}
				}
			} catch (Exception e) {
				log.warn("problem for add engine manager: " + cls_name);
			}
		}
		
		private String getServiceName(Class clazz) throws CIBusException {
			if (clazz.isAnnotationPresent(EngineManager.class)) {
				EngineManager em =  (EngineManager) clazz.getAnnotation(EngineManager.class);
				return em.service_name();
			}
			return null;
		}
			
		private boolean isManager(String cls) throws CIBusException {
			Class clazz = ProxyUtil.loadClass(cls, BusOsgiUtil.getBundleClassLoader(m_context));
			if (clazz.isAnnotationPresent(EngineManager.class)) {
				return true;
			}
			return false;
		}
		
		private void loadManager(Class clazz, int type, boolean init) throws CIBusException {
			String clazzName = clazz.getName();
			try {
				boolean isPublishType = false;
				if (type == 1) // publish part 
					isPublishType =  clazz.isAnnotationPresent(EnginePart.class);
				if (type == 2) // publish service
					isPublishType =  clazz.isAnnotationPresent(EngineService.class);
				if (isPublishType) {
					BusEngineManager manager = (BusEngineManager) clazz.newInstance();
					boolean load = true;
					if (init) {
						Properties props = new Properties();
						props.load(manager.getClass().getResourceAsStream("manager.properties"));
						String autoload = PropertiesUtil.getString(props, "autoload", "on");
						int priority = PropertiesUtil.getInt(props, "priority", 1);
						load = false;
						if ("on".equalsIgnoreCase(autoload.trim()))
							load = true;
						ManagerParameters parameters = new ManagerParameters(load, EU_ManagerState.ACTIVE, priority);
						manager.setParameters(parameters);
						parametersMap.put(clazzName, parameters);
					}
					
					if (load) {
						String serviceName = getServiceName(clazz);
						if (serviceName != null) {
							BusOsgiUtil.addServiceToContext(m_context, manager, serviceName);
							manager.regist(m_context);
							managerMap.put(clazzName, manager);
							log.info("add engine manager: " + clazzName);
						}
					}
				}
			} catch (Exception e) {
				throw new CIBusException("", "add engine manager error", e);
			}
		}
		
		private void registToContext(BusEngineManager service) {
			Class clazz = service.getClass();
			EngineManager em =  (EngineManager) clazz.getAnnotation(EngineManager.class);
			BusOsgiUtil.addServiceToContext(m_context, service, em.service_name());
		}
	}
}