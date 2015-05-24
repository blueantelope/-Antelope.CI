// com.antelope.ci.bus.server.auth.UserPubkeyInfo.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model.user;

import com.antelope.ci.bus.common.EncryptUtil.ASYMMETRIC_ALGORITHM;
import com.antelope.ci.bus.engine.model.BaseModel;
import com.antelope.ci.bus.engine.model.Model;

/**
 * 用户密钥信息
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		上午10:49:22 
 */
@Model
public class Key extends BaseModel {
	private ASYMMETRIC_ALGORITHM algorithm;
	private String privateKey;
	private String publicKey;
	private String passphase;
	
	// getter and setter
	public ASYMMETRIC_ALGORITHM getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(ASYMMETRIC_ALGORITHM algorithm) {
		this.algorithm = algorithm;
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
	@Override
	protected void init() {
		
		// TODO Auto-generated method stub
		
	}
}

