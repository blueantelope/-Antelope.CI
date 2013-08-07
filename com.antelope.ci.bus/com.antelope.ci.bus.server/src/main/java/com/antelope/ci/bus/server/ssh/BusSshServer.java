// com.antelope.ci.bus.server.SshServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh;

import java.io.IOException;

import org.apache.sshd.SshServer;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellFactory;


/**
 * ssh server服务器
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-7		上午11:23:03 
 */
public class BusSshServer {
	private final static String KYE_NAME = "bus_key.ser";
	private SshServer sshServer;
	private int port;					// server提供的端口
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void start() throws IOException {
		sshServer = SshServer.setUpDefaultServer();
		sshServer.setPort(port);
		sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(KYE_NAME));
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
}

