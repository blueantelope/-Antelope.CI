// com.antelope.ci.bus.server.auth.AuthImpl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.server.service.auth;

import java.util.Map;

import com.antelope.ci.bus.engine.model.user.User;
import com.antelope.ci.bus.engine.model.user.User.AUTH_TYPE;
import com.antelope.ci.bus.osgi.IService;

/**
 * 登录验证service
 * 集成了密码验证和证书验证两种方式
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午4:56:20
 */
public interface AuthService extends IService {
	public static final String NAME = "com.antelope.ci.bus.server.service.auth.AuthService";
	
	public void initUserStore(Map<String, User> userStore);
	
	public abstract AUTH_TYPE getAuthType();
}
