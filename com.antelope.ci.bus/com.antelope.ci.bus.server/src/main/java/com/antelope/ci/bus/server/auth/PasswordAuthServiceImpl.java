// com.antelope.ci.bus.server.auth.PasswordAuthServiceImpl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.auth;

import java.security.PublicKey;

import org.apache.log4j.Logger;
import org.apache.sshd.server.session.ServerSession;

import com.antelope.ci.bus.common.EncryptUtil.CIPHER;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.BusServerActivator;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-14		下午4:58:51 
 */
public class PasswordAuthServiceImpl implements AuthService {
	private static Logger log;
	private String username;
	private String password;
	private CIPHER cipher;
	
	public PasswordAuthServiceImpl(String username, String password, CIPHER cipher) {
		this.username = username;
		this.password = password;
		this.cipher = cipher;
		try {
			log = BusServerActivator.log4j(PasswordAuthServiceImpl.class);
		} catch (CIBusException e) {
			
		}
	}

	@Override
	public boolean authenticate(String username, String password, ServerSession session) {
		if (username == null || password == null) {
			log.info("用户名或密码为空!");
			return false;
		}
		
		return false;
	}

	@Override
	public boolean authenticate(String username, PublicKey key, ServerSession session) {
		return false;
	}

}

