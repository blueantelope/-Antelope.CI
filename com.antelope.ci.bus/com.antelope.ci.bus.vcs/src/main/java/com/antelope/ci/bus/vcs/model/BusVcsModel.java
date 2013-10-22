// com.antelope.ci.bus.vcs.BusVcs.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.vcs.model;

import java.io.File;

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
	
	protected VCS_TYPE type;
	protected VCS_PROTOCOL protocol;
	protected String url;
	protected String username;
	protected String password;
	protected String email;
	protected String reposPath;
	
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
	public File getRepository() throws CIBusException {
		if (reposPath != null && reposPath.length() > 0) {
			File repos_file = new File(reposPath);
			if (repos_file.exists() && repos_file.isDirectory())
				return repos_file;
			throw new CIBusException("", "repository not exist or not a directory");
		}
		throw new CIBusException("", "reposity path is null");
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
	}
}

