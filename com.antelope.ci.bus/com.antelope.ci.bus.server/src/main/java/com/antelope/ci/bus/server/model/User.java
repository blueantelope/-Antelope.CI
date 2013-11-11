// com.antelope.ci.bus.server.auth.UserAuthInfo.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.model;

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
		OPNE(1<<0, "open"),						// 无验证，开放式
		DEFINE(1<<1, "define"),					// 自定义验证
		PASSWORD(1<<2, "password"),		// 用户名密码方式
		PUBLICKEY(1<<3, "publickey");		// 密钥方式
		
		private int code;
		private String name;
		private AUTH_TYPE(int code, String name) {
			this.code = code;
			this.name = name;
		}
		public int getCode() {
			return code;
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
	
	private int auth_type;
	private String username;
	private UserKey key;
	private UserPassword password;
	
	public User() {
		auth_type = AUTH_TYPE.PASSWORD.code | AUTH_TYPE.PUBLICKEY.code;
	}
	
	public int getAuth_type() {
		return auth_type;
	}
	public void setAuth_type(int auth_type) {
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

