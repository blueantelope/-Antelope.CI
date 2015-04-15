// com.antelope.ci.bus.osgi.InjectHelper.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.osgi;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月10日		下午4:39:53 
 */
public class InjectHelper {
	public static void injectService(Object obj, Map<String, List<BusServiceInfo>> serviceMap) throws CIBusException {
		for (Method method : obj.getClass().getMethods()) {
			if (method.isAnnotationPresent(Inject.class) &&
					method.getName().startsWith("set") &&
					method.getParameterTypes().length == 1) {
				Inject inject = (Inject) method.getAnnotation(Inject.class);
				if ("".equals(inject.name())) continue;
				List<BusServiceInfo> infoList =  serviceMap.get(inject.name());
				if (infoList != null && !infoList.isEmpty()) {
					Object service = infoList.get(0).getService();
					Object[] args = new Object[] {service};
					ProxyUtil.invoke(obj, method, args);
				}
			}
		}
	}
	
	public static void injectService(
			Object obj,
			Map<String, List<BusServiceInfo>> serviceMap,
			List<Method> setterList) throws CIBusException {
		for (Method method : obj.getClass().getMethods()) {
			if (method.isAnnotationPresent(Inject.class) &&
					method.getName().startsWith("set") &&
					method.getParameterTypes().length == 1) {
				Inject inject = (Inject) method.getAnnotation(Inject.class);
				if ("".equals(inject.name())) continue;
				List<BusServiceInfo> infoList =  serviceMap.get(inject.name());
				if (infoList != null && !infoList.isEmpty()) {
					Object service = infoList.get(0).getService();
					Object[] args = new Object[] {service};
					ProxyUtil.invoke(obj, method, args);
					for (Method setter : setterList) {
						if (setter == method) {
							setterList.remove(setter);
							break;
						}
					}
				} else {
					boolean exist = false;
					for (Method setter : setterList) {
						if (setter.equals(method)) {
							exist = true;
							break;
						}
					}
					if (!exist)
						setterList.add(method);
				}
			}
		}
	}
	
	public static void injectService(Object obj, List<Method> setterList, Map<String, List<BusServiceInfo>> serviceMap) throws CIBusException {
		for (Method setter : setterList) {
			if (setter.isAnnotationPresent(Inject.class)) {
				Inject inject = (Inject) setter.getAnnotation(Inject.class);
				if ("".equals(inject.name())) continue;
				List<BusServiceInfo> infoList =  serviceMap.get(inject.name());
				if (infoList != null && !infoList.isEmpty()) {
					Object service = infoList.get(0).getService();
					Object[] args = new Object[] {service};
					ProxyUtil.invoke(obj, setter, args);
				}
			}
		}
	}
}
