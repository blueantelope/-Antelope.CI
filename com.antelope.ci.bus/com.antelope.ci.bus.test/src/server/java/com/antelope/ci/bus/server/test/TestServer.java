// com.antelope.ci.bus.server.test.TestServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.server.test;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.sshd.SshServer;
import org.apache.sshd.common.keyprovider.ResourceKeyPairProvider;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.session.SessionFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * TODO 描述
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-6-9 下午2:20:30
 */
public class TestServer implements BundleActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		PropertyConfigurator.configure(TestServer.class.getResourceAsStream("/log4j.properties"));
		Logger.getLogger(TestServer.class).debug("debug mode");
		start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

	private static void start() throws IOException {
		SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setPort(9022);
		sshd.setKeyPairProvider(new ResourceKeyPairProvider(
				new String[] { TestServer.class.getResource("/hostkey.pem").getPath() }));
		sshd.setShellFactory(new EchoShellFactory());
		sshd.setCommandFactory(new ScpCommandFactory());
		sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
			@Override
			public boolean authenticate(String username, String password,
					ServerSession session) {
				return true;
			}
			
		});
		
		SessionFactory sessionFactory = new SessionFactoryExt();
		sshd.setSessionFactory(sessionFactory);
		sshd.start();
		System.out.println("ssh server startup");
	}

	public static void main(String[] args) throws IOException {
		start();
	}
}
