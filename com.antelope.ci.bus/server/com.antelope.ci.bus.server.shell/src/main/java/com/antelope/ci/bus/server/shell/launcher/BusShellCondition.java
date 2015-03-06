// com.antelope.ci.bus.shell.BusShellCondition.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * shell条件参数
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月12日		下午1:14:46 
 */
public class BusShellCondition {
	private final static String DEFAULT_SHELL = "shell.default";
	protected ClassLoader classLoader;
	protected Map<String, String> shellClassMap;
	
	public BusShellCondition() {
		super();
		init(null);
	}
	
	public BusShellCondition(ClassLoader classLoader) {
		super();
		init(classLoader);
	}
	
	private void init(ClassLoader classLoader) {
		if (classLoader == null) {
			this.classLoader = Thread.currentThread().getContextClassLoader();
		} else {
			this.classLoader = classLoader;
		}
		shellClassMap = new ConcurrentHashMap<String, String>();
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	// shell class
	public Map<String, String> getShellClassMap() {
		return shellClassMap;
	}

	public void setShellClassMap(Map<String, String> shellClassMap) {
		this.shellClassMap = shellClassMap;
	}

	public void addShellClass(String shellClass) {
		String default_shell = shellClassMap.get(DEFAULT_SHELL);
		if (default_shell != null)
			shellClassMap.remove(DEFAULT_SHELL);
		for (String shell : shellClassMap.keySet())
			if (shell.equals(shellClass))
				return;
		
		shellClassMap.put(shellClass, shellClass);
	}
	
	public void addDefaultShellClass(String shellClass) {
		shellClassMap.put(DEFAULT_SHELL, shellClass);
	}
	
	public void removeShellClass(String shellClass) {
		for (String shell : shellClassMap.keySet())
			if (shell.equals(shellClass)) return;
	
		shellClassMap.remove(shellClass);
	}
	
	public List<String> getShellClassList() {
		List<String> shellClassList = new ArrayList<String>();
		for (String shell : shellClassMap.keySet())
			shellClassList.add(shellClassMap.get(shell));
		return shellClassList;
	}
	
	public boolean isShellEmpty() {
		return shellClassMap.isEmpty();
	}
	
	
}