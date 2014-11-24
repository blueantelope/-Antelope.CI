// com.antelope.ci.bus.vcs.BusVcs.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model.vcs.input;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * VCS data model
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-19		下午3:48:30 
 */
public class BusVcsModel {
	public enum VCS_TYPE {
		CVS("cvs"),
		SVN("SVN"),
		GIT("git");
		
		private String name;
		private VCS_TYPE(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String toString() {
			return name;
		}
		public static VCS_TYPE toType(String name) throws CIBusException {
			if (name == null || name.length() < 1)
				throw new CIBusException("", "CVS name is null");
			for (VCS_TYPE type : VCS_TYPE.values()) {
				if (type.getName().equalsIgnoreCase(name.trim()))
					return type;
			}
			throw new CIBusException("", "unknow VCS:" + name);
		}
	}
	
	public enum VCS_PROTOCOL {
		SSH("ssh"),
		HTTP("http"),
		HTTPS("https"),
		CVS("cvs"),
		SVN("svn"),
		GIT("git");
		
		private String name;
		private VCS_PROTOCOL(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String toString() {
			return name;
		}
		public static VCS_PROTOCOL toProtocol(String name) throws CIBusException {
			if (name != null && name.length() > 0) {
				for (VCS_PROTOCOL protocol : VCS_PROTOCOL.values()) {
					if (protocol.name.equalsIgnoreCase(name.trim()))
						return protocol;
				}
			}
			
			throw new CIBusException("", "undefined protocol fo vcs : " + name);
		}
	}
	
	public enum AccessType {
		LOCAL("local"),
		REMOTE("remote");
		private String name;
		private AccessType(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String toString() {
			return name;
		}
		public static AccessType toType(String name) throws CIBusException {
			if (name != null && name.length() > 0) {
				for (AccessType type : AccessType.values()) {
					if (type.getName().equalsIgnoreCase(name.trim()))
						return type;
				}
				throw new CIBusException("", "unknow VCS access type : " + name);
			}
			throw new CIBusException("", " VCS access type is null");
		}
	}
	
	public enum AUTH_TYPE {
		PASSWORD("password"),
		KEY("key"),
		PASSKEY("passkey");
		
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
		public static AUTH_TYPE toType(String name) throws CIBusException {
			if (name != null && name.length() > 0) {
				for (AUTH_TYPE type : AUTH_TYPE.values()) {
					if (type.getName().equalsIgnoreCase(name.trim()))
						return type;
				}
				throw new CIBusException("", "unknow VCS auth type : " + name);
			}
			throw new CIBusException("", " VCS auth type is null");
		}
	}
	
	protected VCS_TYPE type;
	protected VCS_PROTOCOL protocol;
	protected String url;
	protected String username;
	protected String password;
	protected String email;
	protected String reposPath;
	protected String proxy;
	protected AccessType accessType;
	protected String privateKey;
	protected String publicKey;
	protected String passphase;
	protected List<String> specList;
	protected AUTH_TYPE authType;
	
	public BusVcsModel() {
		this.accessType = AccessType.LOCAL;
		specList = new ArrayList<String>();
		specList.add("refs/heads/master");
		specList.add("refs/remotes/origin/master");
	}
	
	// getter and setter
	public VCS_TYPE getType() {
		return type;
	}
	public void setType(VCS_TYPE type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public VCS_PROTOCOL getProtocol() {
		return protocol;
	}
	public void setProtocol(VCS_PROTOCOL protocol) {
		this.protocol = protocol;
	}
	public String getReposPath() {
		return reposPath;
	}
	public void setReposPath(String reposPath) {
		this.reposPath = reposPath;
	}
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	public AccessType getAccessType() {
		return accessType;
	}
	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
	}
	public void setAccessType(String typeName) throws CIBusException {
		this.accessType = AccessType.toType(typeName);
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getPassphase() {
		return passphase;
	}
	public void setPassphase(String passphase) {
		this.passphase = passphase;
	}
	public List<String> getSpecList() {
		return specList;
	}
	public void setSpecList(List<String> specList) {
		this.specList = specList;
		specList.add(0, "refs/heads/master");
		specList.add(1, "refs/remotes/origin/master");
	}
	public void addSpec(String spec) {
		specList.add(spec);
	}
	public AUTH_TYPE getAuthType() {
		return authType;
	}
	public void setAuthType(AUTH_TYPE authType) {
		this.authType = authType;
	}

	public File getRepository() throws CIBusException {
		if (reposPath != null && reposPath.length() > 0) {
			File repos_file = new File(reposPath);
			if (repos_file.exists() && repos_file.isDirectory())
				return repos_file;
			throw new CIBusException("", "repository not exist or not a directory");
		}
		throw new CIBusException("", "reposity path is null");
	}
	
	public String convertSpec() {
		StringBuffer buf = new StringBuffer();
		for (String spec : specList) {
			buf.append(spec).append(":");
		}
		return buf.toString().substring(0, buf.length()-1);
	}
	
	public void setInfo(BusVcsModel model) {
		if (model.type != null)
			this.type = model.type;
		if (model.protocol != null)
			this.protocol = model.protocol;
		if (model.url != null && model.url.length() > 0)
			this.url = model.url;
		if (model.username != null && model.username.length() > 0)
			this.username = model.username;
		if (model.password != null && model.password.length() > 0)
			this.password = model.password;
		if (model.email != null && model.email.length() > 0)
			this.email = model.email;
		if (model.reposPath != null && model.reposPath.length() > 0)
			this.reposPath = model.reposPath;
		if (model.accessType != null)
			this.accessType = model.accessType;
		if (model.privateKey != null && model.privateKey.length() > 0)
			this.privateKey = model.privateKey;
		if (model.publicKey != null && model.publicKey.length() > 0)
			this.publicKey = model.publicKey;
		if (model.passphase != null && model.passphase.length() > 0)
			this.passphase = model.passphase;
		this.specList = model.getSpecList();
		if (model.authType != null)
			this.authType = model.authType;
	}
}

