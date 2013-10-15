// com.antelope.ci.bus.server.service.impl.AbstractAuthService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service.impl;

import java.security.PublicKey;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.EncryptUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.BusServerActivator;
import com.antelope.ci.bus.server.service.AuthService;
import com.antelope.ci.bus.server.service.user.User;
import com.antelope.ci.bus.server.service.user.UserKey;
import com.antelope.ci.bus.server.service.user.UserPassword;

/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		下午12:55:02 
 */
public abstract class AbstractAuthService implements AuthService {
	protected static final String SERVICE_NAME = "com.antelope.ci.bus.server.service.AuthService";
	protected static Logger log;
	protected Map<String, User> userMap = new HashMap<String, User>();
	
	
	public AbstractAuthService(Map<String, User> userMap) throws CIBusException {
		if (userMap != null)
			this.userMap = userMap;
		try {
			log = BusServerActivator.log4j(this.getClass());
		} catch (CIBusException e) {
			log.error(e);
		}
	}
	
	// 得到用户信息
	protected User getUser(String username) {
		if (username == null) {
			log.info("用户名为空!");
			return null;
		}
		if (userMap.get(username) != null)
			return userMap.get(username);
		
		log.info("系统上无此用户");
		return null;
	}
	
	// 密码验证
	protected boolean validtePassword(User user, String password) {
		if (password == null) {
			log.info("密码为空!");
			return false;
		}
		UserPassword u_password = user.getPassword();
		if (u_password == null) {
			log.info("用户密码信息不存在!");
			return false;
		}
		try {
			String password_src = EncryptUtil.decrypt_symmetric(u_password.getAlgorithm(), u_password.getSeed(), password);
			if (u_password.getPassword().equals(password_src)) {
				log.debug("password login succesful");
				return true;
			}
		} catch (CIBusException e) {
			log.error(e);
		}
		
		return false;
	}
	
	// 公钥验证
	protected boolean validtePublickey(User user, PublicKey publicKey) {
		if (publicKey == null) {
			log.info("公钥为空!");
			return false;
		}
		UserKey userKey = user.getKey();
		if (userKey == null) {
			log.info("用户密钥信息不存在!");
			return false;
		}
		String user_pubkey = userKey.getPublicKey();
		try {
			return EncryptUtil.verify_asymmetric(publicKey, user_pubkey);
		} catch (CIBusException e) {
			log.error(e);
		}
		
		return false;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.service.Service#register(org.osgi.framework.BundleContext, com.antelope.ci.bus.server.service.Service)
	 */
	@Override
	public void register(BundleContext m_context) throws CIBusException {
		Dictionary<String, ?> properties = new Hashtable<String, Object>();
		m_context.registerService(SERVICE_NAME, this, properties);
	}
}

