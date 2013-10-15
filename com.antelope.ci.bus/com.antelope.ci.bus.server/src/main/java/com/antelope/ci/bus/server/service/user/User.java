// com.antelope.ci.bus.server.auth.UserAuthInfo.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service.user;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		上午10:38:57 
 */
public class User {
	public enum AUTH_TYPE {
		OPNE("open"),						// 无验证，开放式
		DEFINE("define"),					// 自定义验证
		PASSWORD("password"),		// 用户名密码方式
		PUBLICKEY("publickey"),			// 密钥方式
		PWDKEY("pwdkey");				// 用户名密码和密钥方式
		
		private String name;
		private AUTH_TYPE(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String toString() {
			return name;
		}
		public static AUTH_TYPE toAuthType(String name) throws CIBusException {
			for (AUTH_TYPE at : AUTH_TYPE.values()) {
				if (at.getName().equalsIgnoreCase(name.trim()))
					return at;
			}
			
			throw new CIBusException("", "unknow auth type");
		}
	}
	
	private AUTH_TYPE auth_type;
	private String username;
	private UserKey key;
	private UserPassword password;
	
	public User() {
		
	}
	
	public AUTH_TYPE getAuth_type() {
		return auth_type;
	}
	public void setAuth_type(AUTH_TYPE auth_type) {
		this.auth_type = auth_type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public UserKey getKey() {
		return key;
	}
	public void setKey(UserKey key) {
		this.key = key;
	}
	public UserPassword getPassword() {
		return password;
	}
	public void setPassword(UserPassword password) {
		this.password = password;
	}
}

