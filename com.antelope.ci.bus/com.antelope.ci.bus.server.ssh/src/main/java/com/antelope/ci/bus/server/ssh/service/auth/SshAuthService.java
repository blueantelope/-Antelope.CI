// com.antelope.ci.bus.server.ssh.service.auth.CommonSshAuthService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh.service.auth;

import java.util.Map;

import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.PublickeyAuthenticator;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.model.user.User;
import com.antelope.ci.bus.server.service.auth.AbstractAuthService;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月4日		下午5:05:55 
 */
public abstract class SshAuthService extends AbstractAuthService 
														implements PasswordAuthenticator, PublickeyAuthenticator {

	public SshAuthService() {
		super();
	}
	
	public SshAuthService(Map<String, User> userMap) throws CIBusException {
		super(userMap);
	}
}
