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
	
	private Map<String, User> userMap;
	private SERVER_TYPE serverType;
	private List<AuthService> authServiceList;
	
	public BusServerCondition() {
		userMap = new HashMap<String, User>();
		authServiceList = new ArrayList<AuthService>();
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
}
