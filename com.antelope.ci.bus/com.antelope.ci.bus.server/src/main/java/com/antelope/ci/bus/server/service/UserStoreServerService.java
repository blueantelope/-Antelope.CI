// com.antelope.ci.bus.server.service.UserStoreService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service;

import java.util.Map;

import com.antelope.ci.bus.engine.model.user.User;


/**
 * 用户存储服务
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		下午12:36:18 
 */
public interface UserStoreServerService extends BusServerService {
	public static final String SERVICE_NAME = "com.antelope.ci.bus.server.service.UserStoreServerService";
	
	public Map<String, User> getUserMap();
	
	public User getUser(String username);
}

