// com.antelope.ci.bus.server.BusServerCondition.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.model.user.User;
import com.antelope.ci.bus.server.service.auth.AuthService;


/**
 * Server条件
 * @author   blueantelope
 * @version  0.1 * @Date	 2013-10-17		下午11:30:48 
 */
public class BusServerCondition {
	public enum SERVER_TYPE {
		SHELL("shell"),
		API("api");
		
		private String name;
		private SERVER_TYPE(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public static SERVER_TYPE fromName(String name) {
			for (SERVER_TYPE type : SERVER_TYPE.values()) {
				if (name.equalsIgnoreCase(type.getName())) {
					return type;
				}
			}
			
			return SHELL;
		}
	}
	
	public enum LAUNCHER_TYPE {
		PROXY("proxy"),
		CONTAINER("container");
		
		private String name;
		private LAUNCHER_TYPE(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public static LAUNCHER_TYPE fromName(String name) throws CIBusException {
			for (LAUNCHER_TYPE type : LAUNCHER_TYPE.values()) {
				if (name.equalsIgnoreCase(type.getName())) {
					return type;
				}
			}
			
			throw new CIBusException("", "unknow server laucher type.");
		}
	}
	
	private final static String DEFAULT_SHELL = "shell.default";
	private Map<String, User> userMap;
	private SERVER_TYPE serverType;
	private LAUNCHER_TYPE launcherType;
	private Map<String, String> shellClassMap;
	private Class launcher_class;
	private String launcher_className;
	private List<AuthService> authServiceList;
	
	public BusServerCondition() {
		userMap = new HashMap<String, User>();
		authServiceList = new ArrayList<AuthService>();
		shellClassMap = new ConcurrentHashMap<String, String>();
		serverType = SERVER_TYPE.SHELL;
	}
	
	/* getter and setter */
	// userMap
	public Map<String, User> getUserMap() {
		return userMap;
	}
	public void setUserMap(Map<String, User> userMap) {
		this.userMap = userMap;
	}
	public void addUser(User user) {
		userMap.put(user.getUsername(), user);
	}
	
	// launcher class
	public Class getLauncher_class() {
		return launcher_class;
	}
	public void setLauncher_class(Class launcher_class) {
		this.launcher_class = launcher_class;
	}
	
	// launcher class name
	public String getLauncher_className() {
		return launcher_className;
	}
	public void setLauncher_className(String launcher_className) {
		this.launcher_className = launcher_className;
	}

	// auth service
	public List<AuthService> getAuthServiceList() {
		return authServiceList;
	}
	public void setAuthServiceList(List<AuthService> authServiceList) {
		this.authServiceList = authServiceList;
	}
	public void addAuthService(AuthService authService) {
		authServiceList.add(authService);
	}
	
	// server type
	public SERVER_TYPE getServerType() {
		return serverType;
	}
	public void setServerType(SERVER_TYPE serverType) {
		this.serverType = serverType;
	}
	public void setServerType(String serverTypeName) throws CIBusException {
		this.serverType = SERVER_TYPE.fromName(serverTypeName);
	}

	// launcher type
	public LAUNCHER_TYPE getLauncherType() {
		return launcherType;
	}
	public void setLauncherType(LAUNCHER_TYPE launcherType) {
		this.launcherType = launcherType;
	}
	public void setLauncherType(String launcherTypeName) throws CIBusException {
		this.launcherType = LAUNCHER_TYPE.fromName(launcherTypeName);
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
			if (shell.equals(shellClass)) return;
		
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
