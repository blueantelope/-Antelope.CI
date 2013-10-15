// com.antelope.ci.bus.server.auth.PwdAndKeyAuthenticatorImpl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service.impl;

import java.security.PublicKey;
import java.util.Map;

import org.apache.sshd.server.session.ServerSession;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.service.user.User;



/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		上午9:36:20 
 */
public class PwdAndKeyAuthServiceImpl extends AbstractAuthService {

	public PwdAndKeyAuthServiceImpl(Map<String, User> userMap) throws CIBusException {
		super(userMap);
	}

	@Override
	public boolean authenticate(String username, String password, ServerSession session) {
		return false;
		
	}

	@Override
	public boolean authenticate(String username, PublicKey key,
			ServerSession session) {
		return false;
		
	}

}

