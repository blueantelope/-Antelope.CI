// com.antelope.ci.bus.server.SshServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

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
	private SshServer sshd;
	
	public void start() throws IOException {
		SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setPort(9022);
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("key.ser"));
		sshd.setShellFactory(new ProcessShellFactory());
		sshd.setCommandFactory(new ScpCommandFactory());
		sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
			@Override
			public boolean authenticate(String username, String password,
					ServerSession session) {
				return true;
			}
			
		});
		
		sshd.start();
		System.out.println("ssh server startup");
	}
	
	public void stop() {
		if (sshd != null) {
			try {
				sshd.stop(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

