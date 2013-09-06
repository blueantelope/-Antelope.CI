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

import org.apache.sshd.SshServer;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellFactory;

import com.antelope.ci.bus.common.BusConstants;
import com.antelope.ci.bus.common.FileUtil;


/**
 * 持续集成的server
 * 使用ssh建立server
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-5		下午10:19:24 
 */
public class BusServer {
	private final static String KEY_NAME = "bus_key.ser";
	private SshServer sshServer;
	private int port;					// server提供的端口
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void start() throws IOException {
		sshServer = SshServer.setUpDefaultServer();
		sshServer.setPort(port);
		String key_path = getKeyPath();
		sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(key_path));
		sshServer.setShellFactory(new ProcessShellFactory());
		sshServer.setCommandFactory(new ScpCommandFactory());
		sshServer.setPasswordAuthenticator(new PasswordAuthenticator() {
			@Override
			public boolean authenticate(String username, String password,
					ServerSession session) {
				return true;
			}
			
		});
		
		sshServer.start();
		System.out.println("ssh server startup");
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
	private String getKeyPath() {
		String key_path = KEY_NAME;
		if (System.getProperty(BusConstants.CACHE_DIR) != null) {
			String cache_dir = System.getProperty(BusConstants.CACHE_DIR);
			if (FileUtil.existDir(cache_dir)) {
				key_path = cache_dir + File.separator + KEY_NAME;
			} else {
				cache_dir = System.getProperty("java.io.tmpdir");
				if (FileUtil.existDir(cache_dir)) {
					key_path = cache_dir + File.separator + KEY_NAME;
				}
			}
		}
		
		return key_path;
	}
}
