// com.antelope.ci.bus.gate.api.client.Client.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.connector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月15日		下午3:42:31 
 */
public class ConnectorAdapter {
	private static final String ROOT_PACKAGE = "com.antelope.ci.bus.connector";
	private static Map<String, Class> connectorClassMap;
	
	static {
		connectorClassMap = new HashMap<String, Class>();
		try {
			List<String> classNameList = ClassFinder.findClasspath(ROOT_PACKAGE);
			for (String className : classNameList) {
				Class clazz = ProxyUtil.loadClass(className);
				if (clazz.isAnnotationPresent(Connector.class) &&
						IConnector.class.isAssignableFrom(clazz)) {
					Connector connector = (Connector) clazz.getAnnotation(Connector.class);
					connectorClassMap.put(connector.name(), clazz);
				}
			}
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public static IConnector getConnector(Class invokerClass) throws CIBusException {
		if (invokerClass.isAnnotationPresent(Invoker.class)) {
			Invoker invoker = (Invoker) invokerClass.getAnnotation(Invoker.class);
			if (invoker.connectClazz() == NullConnector.class) {
				String name = invoker.connectName();
				if (!StringUtil.empty(name) && connectorClassMap.containsKey(name))
					return (IConnector) ProxyUtil.newObject(connectorClassMap.get(name));
			} else {
				return (IConnector) ProxyUtil.newObject(invoker.connectClazz());
			}
		}
		
		return null;
	}
}
