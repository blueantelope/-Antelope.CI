// com.antelope.ci.bus.server.shell.BusShellContainer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusActivator;


/**
 * shell容器，支持多个shell切换
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		上午9:31:43 
 */
public class BusShellContainer {
	protected Map<String, String> shellClassMap;
	
	public BusShellContainer() {
		shellClassMap = new HashMap<String, String>();
	}

	public BusShellContainer(List<String> shellClsList) throws CIBusException {
		shellClassMap = new HashMap<String, String>();
		addShell(shellClsList);
	}
	
	public void addShell(List<String> shellClsList) throws CIBusException {
		shellClassMap.clear();
		for (String shellCls : shellClsList) {
			try {
				addShell(shellCls);
			} catch (Exception e) {
				throw new CIBusException("", e);
			}
		}
	}
	
	public void addShell(String shellClass) throws CIBusException {
		String status = BusShellStatus.ROOT;
		Class clazz;
		try {
			clazz = Class.forName(shellClass);
		} catch (ClassNotFoundException e) {
			try {
				clazz = Class.forName(shellClass, false, BusActivator.getClassLoader());
			} catch (ClassNotFoundException e1) {
				DevAssistant.errorln(e1);
				throw new CIBusException("", e1);
			}
		}
		for ( ;BusShell.class.isAssignableFrom(clazz); clazz=clazz.getSuperclass()) {
			String fs = null;
			fs = fetchStatus(clazz);
			if (fs != null) {
				status = fs;
				break;
			}
		}
		if (shellClassMap.get(status) != null)
			throw new CIBusException("", "");
		shellClassMap.put(status, shellClass);
	}
	
	private String fetchStatus(Class clazz) {
		if (clazz.isAnnotationPresent(Shell.class)) {
			Shell sa = (Shell) clazz.getAnnotation(Shell.class);
			return sa.status();
		}
		
		return null;
	}
	
	public BusShell createShell(String status) throws CIBusException {
		String clsName = shellClassMap.get(status);
		if (BusActivator.getClassLoader() != null)
			return (BusShell) ProxyUtil.newObject(clsName, BusActivator.getClassLoader());
		return (BusShell) ProxyUtil.newObject(clsName);
	}

	public String getShellClass(String status) {
		return shellClassMap.get(status);
	}

	public Map<String, String> getShellClassMap() {
		return shellClassMap;
	}
}

