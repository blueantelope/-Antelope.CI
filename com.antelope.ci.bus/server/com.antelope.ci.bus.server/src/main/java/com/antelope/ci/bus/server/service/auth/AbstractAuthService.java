// com.antelope.ci.bus.server.service.impl.AbstractAuthService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service.auth;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.EncryptUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.model.user.Key;
import com.antelope.ci.bus.engine.model.user.Passwd;
import com.antelope.ci.bus.engine.model.user.User;
import com.antelope.ci.bus.engine.model.user.User.AUTH_TYPE;
import com.antelope.ci.bus.osgi.BusContext;
import com.antelope.ci.bus.osgi.BusOsgiUtil;
import com.antelope.ci.bus.osgi.BusOsgiUtil.ServiceProperty;
import com.antelope.ci.bus.server.service.CommonServerService;

/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		下午12:55:02 
 */
public abstract class AbstractAuthService extends CommonServerService implements AuthService {
	private final static Logger log = Logger.getLogger(AbstractAuthService.class);
	public final static String SERVICE_AUTH_TYPE = "service.auth.type";
	protected Map<String, User> userStore = new HashMap<String, User>();
	protected AUTH_TYPE auth_type = null;
	
	public AbstractAuthService() {
		auth_type = initAuthType();
	}
	
	public AbstractAuthService(Map<String, User> userMap) throws CIBusException {
		super();
		if (userMap != null)
			this.userStore = userMap;
	}
	
	public void initUserStore(Map<String, User> userStore) {
		this.userStore = userStore;
	}
	
	// 得到用户信息
	protected User getUser(String username) {
		if (username == null) {
			log.info("用户名为空!");
			return null;
		}
		if (userStore.get(username) != null)
			return userStore.get(username);
		
		log.info("系统上无此用户");
		return null;
	}
	
	// 密码验证
	protected boolean validtePassword(User user, String password) {
		if (password == null) {
			log.info("密码为空!");
			return false;
		}
		Passwd u_password = user.getPassword();
		if (u_password == null) {
			log.info("用户密码信息不存在!");
			return false;
		}
		try {
			String password_src = EncryptUtil.decrypt_symmetric(u_password.getAlgorithm(), u_password.getSeed(), password);
			if (u_password.getPassword().equals(password_src)) {
//				log.debug("password login successful");
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
			log.info("public key is null from key.");
			return false;
		}
		Key userKey = user.getKey();
		if (userKey == null) {
			log.info("user key is null on server.");
			return false;
		}
		String user_pubkey = userKey.getPublicKey();
		if (StringUtil.empty(user_pubkey)) {
			log.info("public key do not exists on server.");
			return false;
		}
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
	 * @see com.antelope.ci.bus.osgi.IService#publish(org.osgi.framework.BundleContext)
	 * @Deprecated replace by {@link #publish(BusContext context)}
	 */
	@Deprecated
	@Override
	public boolean publish(BundleContext m_context) throws CIBusException {
		List<ServiceProperty> otherList = new ArrayList<ServiceProperty>(); 
		if (auth_type != null)
			otherList.add(new ServiceProperty(SERVICE_AUTH_TYPE, auth_type.getName()));
		List<ServiceProperty> extList = extendServiceProperties();
		if (extList != null)
			otherList.addAll(extendServiceProperties());
		BusOsgiUtil.publishService(m_context, this, AuthService.NAME, 
				otherList.toArray(new ServiceProperty[otherList.size()]));
		return true;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.IService#publish(com.antelope.ci.bus.osgi.BusContext)
	 */
	@Override
	public boolean publish(BusContext context) throws CIBusException {
		List<ServiceProperty> otherList = new ArrayList<ServiceProperty>(); 
		if (auth_type != null)
			otherList.add(new ServiceProperty(SERVICE_AUTH_TYPE, auth_type.getName()));
		List<ServiceProperty> extList = extendServiceProperties();
		if (extList != null)
			otherList.addAll(extendServiceProperties());
		BusOsgiUtil.publishService(context.getBundleContext(), this, AuthService.NAME, 
				otherList.toArray(new ServiceProperty[otherList.size()]));
		return true;
	}

	protected abstract AUTH_TYPE initAuthType();
	
	protected abstract List<ServiceProperty> extendServiceProperties();
}