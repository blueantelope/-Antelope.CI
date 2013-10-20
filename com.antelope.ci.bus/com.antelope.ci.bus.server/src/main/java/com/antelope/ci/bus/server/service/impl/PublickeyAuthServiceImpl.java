// com.antelope.ci.bus.server.auth.PublickeyAuthenticatorImpl.java
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
import com.antelope.ci.bus.server.service.user.User.AUTH_TYPE;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-14		下午5:02:20 
 */
public class PublickeyAuthServiceImpl extends AbstractAuthService {

	public PublickeyAuthServiceImpl(Map<String, User> userMap) throws CIBusException {
		super(userMap);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see org.apache.sshd.server.PasswordAuthenticator#authenticate(java.lang.String, java.lang.String, org.apache.sshd.server.session.ServerSession)
	 */
	@Override
	public boolean authenticate(String username, String password, ServerSession session) {
		return false;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see org.apache.sshd.server.PublickeyAuthenticator#authenticate(java.lang.String, java.security.PublicKey, org.apache.sshd.server.session.ServerSession)
	 */
	@Override
	public boolean authenticate(String username, PublicKey key, ServerSession session) {
		User user = null;
		if ((user=getUser(username)) == null )
			return false;
		
		return validtePublickey(user, key);
	}

	@Override
	public AUTH_TYPE getAuthType() {
		return AUTH_TYPE.PUBLICKEY;
	}

}
