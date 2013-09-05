// com.antelope.ci.bus.server.ssh.BusPasswordAuthenticator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.session.ServerSession;

/**
 * ssh用户名密码验证方式
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-7		下午1:56:57 
 */
public class BusPasswordAuthenticator implements PasswordAuthenticator {
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.apache.sshd.server.PasswordAuthenticator#authenticate(java.lang.String, java.lang.String, org.apache.sshd.server.session.ServerSession)
	 */
	@Override
	public boolean authenticate(String username, String password, ServerSession session) {
		
		return true;
	}

}

