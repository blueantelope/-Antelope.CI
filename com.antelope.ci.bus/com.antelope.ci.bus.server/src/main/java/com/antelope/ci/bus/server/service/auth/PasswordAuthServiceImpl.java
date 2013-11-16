// com.antelope.ci.bus.server.auth.PasswordAuthServiceImpl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service.auth;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

import org.apache.sshd.server.session.ServerSession;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.model.user.User;
import com.antelope.ci.bus.model.user.User.AUTH_TYPE;
import com.antelope.ci.bus.osgi.BusOsgiUtil.ServiceProperty;
import com.antelope.ci.bus.server.service.ServerService;


/**
 * 密码登录方式验证
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-14		下午4:58:51 
 */
@ServerService(serviceName="com.antelope.ci.bus.server.service.AuthService")
public class PasswordAuthServiceImpl extends AbstractAuthService {
	
	public PasswordAuthServiceImpl() {
		
	}
	
	public PasswordAuthServiceImpl(Map<String, User> userMap) throws CIBusException {
		super(userMap);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see org.apache.sshd.server.PasswordAuthenticator#authenticate(java.lang.String, java.lang.String, org.apache.sshd.server.session.ServerSession)
	 */
	@Override
	public boolean authenticate(String username, String password, ServerSession session) {
		User user = null;
		if ((user=getUser(username)) == null )
			return false;
		
		return validtePassword(user, password);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see org.apache.sshd.server.PublickeyAuthenticator#authenticate(java.lang.String, java.security.PublicKey, org.apache.sshd.server.session.ServerSession)
	 */
	@Override
	public boolean authenticate(String username, PublicKey key, ServerSession session) {
		return false;
	}

	@Override
	public AUTH_TYPE getAuthType() {
		return AUTH_TYPE.PASSWORD;
	}

	@Override
	protected AUTH_TYPE initAuthType() {
		return AUTH_TYPE.PASSWORD;
	}

	@Override
	protected List<ServiceProperty> extendServiceProperties() {
		
		// TODO Auto-generated method stub
		return null;
		
	}


}

