// com.antelope.ci.bus.server.auth.UserPasswordInfo.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model.user;

import com.antelope.ci.bus.common.EncryptUtil.SYMMETRIC_ALGORITHM;


/**
 * 用户密码信息
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		上午10:48:51 
 */
public class Passwd {
	private SYMMETRIC_ALGORITHM algorithm; 
	private String seed;
	private String originPwd;
	private String password;
	
	// getter and setter
	public SYMMETRIC_ALGORITHM getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(SYMMETRIC_ALGORITHM algorithm) {
		this.algorithm = algorithm;
	}
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOriginPwd() {
		return originPwd;
	}
	public void setOriginPwd(String originPwd) {
		this.originPwd = originPwd;
	}
}
