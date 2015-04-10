// com.antelope.ci.bus.server.auth.UserAuthInfo.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model.user;

import java.util.HashSet;
import java.util.Set;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.model.BaseModel;
import com.antelope.ci.bus.engine.model.Model;


/**
 * login user
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		上午10:38:57 
 */
@Model
public class User extends BaseModel {
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
	
	protected int id;
	protected String username;
	protected Passwd password;
	protected int auth_type;
	protected Group group;
	protected Set<Domain> domainSet;
	protected Key key;
	
	public User() {
		super();
		init();
	}
	
	public User(int id, String username, Group group) {
		this();
		this.id = id;
		this.username = username;
		this.group = group;
	}

	protected void init() {
		auth_type = AUTH_TYPE.PASSWORD.code | AUTH_TYPE.PUBLICKEY.code;
		domainSet = new HashSet<Domain>();
	}
	
	
	// getter and setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	
	public Passwd getPassword() {
		return password;
	}
	public void setPassword(Passwd password) {
		this.password = password;
	}

	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	
	public Set<Domain> getDomainSet() {
		return domainSet;
	}
	public void setDomainSet(Set<Domain> domainSet) {
		this.domainSet = domainSet;
	}
	
	// add base domain
	public void addPortalDomain() {
		try {
			domainSet.add(Domain.newPortal());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public void addPortalShellDomain() {
		try {
			domainSet.add(Domain.newPortalShell());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public void addGateDomain() {
		try {
			domainSet.add(Domain.newGate());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public void addGateApiDomain() {
		try {
			domainSet.add(Domain.newGateApi());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public void addGateShellDomain() {
		try {
			domainSet.add(Domain.newGateShell());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public void addAppDomain() {
		try {
			domainSet.add(Domain.newApp());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public void addAppWebDomain() {
		try {
			domainSet.add(Domain.newAppWeb());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public static Set<User> initUserSet() {
		Set<User> userSet = new HashSet<User>();
		userSet.add(new User(0, "admin", new Group(0)));
		return userSet;
	}
}
