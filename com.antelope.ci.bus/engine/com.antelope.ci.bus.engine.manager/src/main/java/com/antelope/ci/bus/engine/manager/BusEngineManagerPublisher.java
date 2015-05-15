// com.antelope.ci.bus.engine.manager.BusEngineManagerPublisher.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
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


/**
 * manager对外提供servcie管理
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月14日		下午4:21:20 
 */
public class BusEngineManagerPublisher {
	private static final Logger log = Logger.getLogger(EngineManagerPublisher.class);
	private static Map<String, BusEngineManager> managerMap = new ConcurrentHashMap<String, BusEngineManager>();
	private static Map<String, ManagerParameters> parametersMap = new ConcurrentHashMap<String, ManagerParameters>();

	public static void publish(BusEngineManagerContext context, EnginePublishInfo info) {
		new ServicePublishHook(context, info).start();
	}
	
	private static class ServicePublishHook extends Thread {
		private BusEngineManagerContext context;
		private EnginePublishInfo info;
		
		private ServicePublishHook(BusEngineManagerContext context, EnginePublishInfo info) {
			this.context = context;
			this.info = info;
		}
		
		public void run() {
			while (true) {
				try {
					if (info.isCheckPart())
						publish(info.getPartClasspath(), 1);
					if (info.isCheckService())
						publish(info.getServiceClasspath(), 2);
				} catch (CIBusException e) {
					log.warn("publish enging manager: " + e);
				}
				try {
					Thread.sleep(context.getPublishPeriod());
				} catch (InterruptedException e) { }
			}
		}
		
		private void publish(String classpath, int type) throws CIBusException {
			BundleContext m_context = context.getBundleContext();
			ClassLoader classLoader = context.getClassLoader();
			List<String> classNameList = ClassFinder.findClasspath(classpath, classLoader);
			List<String> regList = new ArrayList<String>();
			for (String className : classNameList) {
				try {
					if (!isManager(className)) continue;
					if (managerMap.containsKey(className)) continue;
					
					Class clazz = ProxyUtil.loadClass(className, classLoader);
					String serviceName = getServiceName(clazz);
					if (serviceName == null) continue;
					ServiceReference serviceReference = context.getServiceReference(serviceName, className);
					
					if (serviceReference == null && !parametersMap.containsKey(className)) {
						loadManager(clazz, type, true);
						continue;
					}
					
					ManagerParameters parameters = parametersMap.get(className);
					if (parameters == null) continue;
					
					BusEngineManager manager = managerMap.get(className);
					switch (parameters.getState()) {
						case UNACTIVE:
							if (serviceReference != null) {
								m_context.ungetService(serviceReference);
								manager.unregist(m_context);
								log.info("unactive engine manager: " + className);
							}
							break;
						case ACTIVE:
							if (serviceReference == null) {
								registToContext(manager);
								manager.regist(m_context);
								log.info("active engine manager: " + className);
							}
							break;
						case UNLOAD:
							if (serviceReference != null) {
								m_context.ungetService(serviceReference);
								manager.unregist(m_context);
								managerMap.remove(className);
								log.info("unload engine manager: " + className);
							}
							break;
						case LOAD:
							if (serviceReference == null)
								loadManager(clazz, type, false);
							break;
					}
					parameters.setState(parameters.getState());
				} catch (Exception e) {
					log.warn("problem for add engine manager: " + className + "\n" + e);
				}
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
			Class clazz = ProxyUtil.loadClass(cls, context.getClassLoader());
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
					BusEngineManager manager = (BusEngineManager) ProxyUtil.newObject(clazz, context.getClassLoader());
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
							BundleContext m_context = context.getBundleContext();
							BusOsgiUtil.publishService(m_context, manager, serviceName);
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
			BusOsgiUtil.publishService(context.getBundleContext(), service, em.service_name());
		}
	}
}

