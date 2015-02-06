// com.antelope.ci.bus.server.shell.command.CommandAdapterFactory.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import java.util.List;
import java.util.Vector;

import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.osgi.BusActivator;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-14		下午6:40:55 
 */
public class CommandAdapterFactory {
	private static final Object locker = new Object();
	private static List<String> adapterClassList = new Vector<String>();

	public static CommandAdapter getAdapter(String className) {
		synchronized(locker) {
			for (String adapterClass : adapterClassList) {
				if (adapterClass.equals(className))
					return newAdapter(className);
			}
			adapterClassList.add(className);
			return newAdapter(className);
		}
	}

	private static CommandAdapter newAdapter(String className) {
		Object o = ProxyUtil.newObject(className);
		if (o == null)
			o = ProxyUtil.newObject(className, BusActivator.getClassLoader());
		return (CommandAdapter) o;
	}
}

