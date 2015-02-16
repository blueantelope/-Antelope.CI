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

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-14		下午6:40:55 
 */
public class CommandAdapterFactory {
	private static final Logger log = Logger.getLogger(CommandAdapterFactory.class);
	private static Map<String, CommandAdapter> adapterMap = new ConcurrentHashMap<String, CommandAdapter>();

	public static CommandAdapter getAdapter(String clazz, ClassLoader classLoader) {
		for (String name : adapterMap.keySet()) {
			if (name.equals(clazz))
				return adapterMap.get(name);
		}
		CommandAdapter adapter = newAdapter(clazz, classLoader);
		adapterMap.put(clazz, adapter);
		return adapter;
	}

	private static CommandAdapter newAdapter(String className, ClassLoader classLoader) {
		CommandAdapter adapter;
		if (classLoader != null) {
			adapter = (CommandAdapter) ProxyUtil.newObject(className, classLoader);
			adapter.initClassLoader(classLoader);
		} else {
			adapter = (CommandAdapter) ProxyUtil.newObject(className);
		}
		try {
			adapter.initCommands();
		} catch (CIBusException e) {
			log.error("initialize command error:\n" + e);
		}
		return adapter;
	}
}
