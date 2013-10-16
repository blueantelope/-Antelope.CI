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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;

import com.antelope.ci.bus.common.BusConstants;
import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.service.impl.PasswordAuthServiceImpl;
import com.antelope.ci.bus.server.service.impl.PublickeyAuthServiceImpl;
import com.antelope.ci.bus.server.service.user.User;
import com.antelope.ci.bus.server.shell.BusShellFactory;


/**
 * 持续集成的server
 * 使用ssh建立server
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-5		下午10:19:24 
 */
public class BusServer {
	private SshServer sshServer;
	private BusServerConfig config;					// server配置项
	private Map<String, User> userMap;
	
	public void initUserMap(List<User> userList) {
		userMap = new HashMap<String, User>();
		for (User user : userList) {
			userMap.put(user.getUsername(), user);
		}
	}
	
	public void BusServer() {
		config = new BusServerConfig();
	}
	
	public void setConfig(BusServerConfig config) {
		if (config != null)
			this.config = config;
	}
	
	public void start() throws IOException, CIBusException {
		sshServer = SshServer.setUpDefaultServer();
		sshServer.setPort(config.getPort());
		String key_path;
		switch (config.getKt()) {
			case DYNAMIC:
				key_path = getKeyPath(config.getKey_name());
				sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(key_path));
				break;
			case STATIC:
			default:
				URL key_url = config.getKey_url();
				key_path = key_url==null?"":key_url.getFile();
				sshServer.setKeyPairProvider(new FileKeyPairProvider(new String[] {key_path}));
				break;
		}
		sshServer.setShellFactory(new BusShellFactory());
		sshServer.setPasswordAuthenticator(new PasswordAuthServiceImpl(userMap));
		sshServer.setPublickeyAuthenticator(new PublickeyAuthServiceImpl(userMap));
		
		sshServer.start();
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
}
