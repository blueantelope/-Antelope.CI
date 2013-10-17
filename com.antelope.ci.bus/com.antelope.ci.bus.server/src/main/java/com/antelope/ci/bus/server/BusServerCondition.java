// com.antelope.ci.bus.server.BusServerCondition.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.util.Map;

import com.antelope.ci.bus.server.service.user.User;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-17		下午11:30:48 
 */
public class BusServerCondition {
	private Map<String, User> userMap;
	private Class command_class;
	private String command_className;
	
	
	public Map<String, User> getUserMap() {
		return userMap;
	}
	public void setUserMap(Map<String, User> userMap) {
		this.userMap = userMap;
	}
	public Class getCommand_class() {
		return command_class;
	}
	public void setCommand_class(Class command_class) {
		this.command_class = command_class;
	}
	public String getCommand_className() {
		return command_className;
	}
	public void setCommand_className(String command_className) {
		this.command_className = command_className;
	}
}

