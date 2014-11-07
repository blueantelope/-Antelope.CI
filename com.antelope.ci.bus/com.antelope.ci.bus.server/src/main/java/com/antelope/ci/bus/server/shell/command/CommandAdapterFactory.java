// com.antelope.ci.bus.server.shell.command.CommandAdapterFactory.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.osgi.CommonBusActivator;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-14		下午6:40:55 
 */
public class CommandAdapterFactory {
	private static final Object locker = new Object();
	private static Map<String, CommandAdapter> adapterMap = new ConcurrentHashMap<String, CommandAdapter>();

	public static CommandAdapter getAdapter(String className) {
		synchronized(locker) {
			try {
				if (adapterMap.containsKey(className))
					return adapterMap.get(className);
				Object o = ProxyUtil.newObject(className);
				if (o == null)
					o = ProxyUtil.newObject(className, CommonBusActivator.getClassLoader());
				adapterMap.put(className, (CommandAdapter) o);
				return (CommandAdapter) o;
			} catch (Exception e) {
				DevAssistant.assert_exception(e);
				return null;
			}
		}
	}
}

