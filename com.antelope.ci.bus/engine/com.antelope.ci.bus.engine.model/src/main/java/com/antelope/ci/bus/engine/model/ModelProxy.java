// com.antelope.ci.bus.model.ModelProxy.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * a kind of operator for model
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-1		下午5:35:28 
 */
public class ModelProxy {
	private static Map<String, Class> modelClassMap;
	
	static {
		modelClassMap = new ConcurrentHashMap<String, Class>();
	}
	
	public static void addModelClass(String className, ClassLoader classLoader) throws CIBusException {
		Class clazz = ProxyUtil.loadClass(className, classLoader);
		if (clazz.isAnnotationPresent(Model.class))
			modelClassMap.put(className, clazz);
	}
	
	/**
	 * create model object from map dataset
	 * @param  @param fromMap
	 * @param  @return
	 * @return Object
	 * @throws CIBusException 
	 */
	public static Object createModel(String className, Map<String, Object> dataMap) throws CIBusException {
		if (modelClassMap.containsKey(className)) {
			Object model = ProxyUtil.createObject(modelClassMap.get(className));
			for (Method method : model.getClass().getMethods()) {
				if (method.isAnnotationPresent(ModelData.class)) {
					ModelData modelData = (ModelData) method.getAnnotation(ModelData.class);
					String dataName = modelData.name();
					if (dataMap.containsKey(dataName)) {
						Object[] args = new Object[] {dataMap.get(dataName)};
						ProxyUtil.invoke(model, method, args);
						break;
					}
				}
			}
			
			return model;
		}
		
		return null;
	}
	
	private static boolean setModelData(Object model, String varName, Map<String, Object> dataMap) throws CIBusException {
		for (Method method : model.getClass().getMethods()) {
			if (method.isAnnotationPresent(ModelData.class)) {
				ModelData modelData = (ModelData) method.getAnnotation(ModelData.class);
				Object[] args = new Object[] {dataMap.get(varName)};
				ProxyUtil.invoke(model, method, args);
				return true;
			}
		}
		
		return false;
	}
	
	public static Map<String, Map<String, Object>> classify(Map<String, Object> fromMap) {
		Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
		for (String name : fromMap.keySet()) {
			String[] names = name.split(".");
			if (names.length != 2)
				continue;
			String className = names[0];
			String varName = names[1];
			
			Map<String, Object> classMap;
			if (resultMap.containsKey(className)) {
				classMap = resultMap.get(className);
			} else {
				classMap = new HashMap<String, Object>();
				resultMap.put(className, classMap);
			}
			classMap.put(varName, fromMap.get(name));
		}
		return resultMap;
	}
}

