// com.antelope.ci.bus.server.BusServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;

import com.antelope.ci.bus.common.BusConstants;
import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.service.auth.AbstractAuthService;
import com.antelope.ci.bus.server.shell.BusShellFactory;


/**
 * 持续集成的server
 * 使用ssh建立server
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-5		下午10:19:24 
 */
public abstract class BusServer {
	protected SshServer sshServer;
	protected BusServerConfig config;					// server配置项
	protected BusServerCondition condition;
	
	public BusServer() throws CIBusException {
		init();
	}
	
	private void init() throws CIBusException {
		config = readConfig();
		condition = attatchCondition();
	}
	
	public void start() throws CIBusException {
		sshServer = SshServer.setUpDefaultServer();
		sshServer.setPort(config.getPort());
		String key_path;
		switch (config.getKt()) {
			case DYNAMIC:
				key_path = getKeyPath(config.getKey_name());
				sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(key_path));
				break;
			case FIXED:
			default:
				URL key_url = config.getKey_url();
				key_path = key_url==null?"":key_url.getFile();
				sshServer.setKeyPairProvider(new FileKeyPairProvider(new String[] {key_path}));
				break;
		}
		BusShellFactory shellFactory;
		if (condition.getCommand_class() != null) {
			shellFactory = new BusShellFactory(condition.getCommand_class());
		} else if (condition.getCommand_className() != null && condition.getCommand_className().length() > 0) {
			shellFactory = new BusShellFactory(condition.getCommand_className());
		} else {
			throw new CIBusException("", "create shell factory error");
		}
		sshServer.setShellFactory(shellFactory);
		for (AbstractAuthService authService : condition.getAuthServiceList()) {
			switch (authService.getAuthType()) {
				case PASSWORD:
					sshServer.setPasswordAuthenticator(authService);
					break;
				case PUBLICKEY:
					sshServer.setPublickeyAuthenticator(authService);
					break;
			}
		}
		try {
			sshServer.start();
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	public void stop() {
		if (sshServer != null) {
			try {
				sshServer.stop(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 得到密钥所在路径
	 * 先使用ci bus的缓存目录
	 * 如果不存在，使用系统的缓存目录
	 * 如果都不存在，放在与类同一级目录下
	 */
	private String getKeyPath(String key_name) {
		String key_path = key_name;
		if (System.getProperty(BusConstants.CACHE_DIR) != null) {
			String cache_dir = System.getProperty(BusConstants.CACHE_DIR);
			if (FileUtil.existDir(cache_dir)) {
				key_path = cache_dir + File.separator + key_name;
			} else {
				cache_dir = System.getProperty("java.io.tmpdir");
				if (FileUtil.existDir(cache_dir)) {
					key_path = cache_dir + File.separator + key_name;
				}
			}
		}
		
		return key_path;
	}
	
	/*
	 * 读取服务配置
	 */
	protected abstract BusServerConfig readConfig() throws CIBusException;
	
	/*
	 * 加入条件变量
	 */
	protected abstract BusServerCondition attatchCondition() throws CIBusException;
}
