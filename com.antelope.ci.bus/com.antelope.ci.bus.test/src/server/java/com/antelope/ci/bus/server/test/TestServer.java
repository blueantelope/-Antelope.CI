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
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.session.SessionFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

/**
 * TODO 描述
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-6-9 下午2:20:30
 */
public class TestServer implements BundleActivator, ServiceListener {
	@Override
	public void start(BundleContext context) throws Exception {
		context.addServiceListener(this);
//		start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

	private static void start() throws IOException {
		SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setPort(9022);
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("key.ser"));
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
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.ServiceEvent)
	 */
	@Override
	public void serviceChanged(ServiceEvent event) {
		System.out.println("监控service");
		String[] objectClass = (String[]) event.getServiceReference().getProperty("objectClass");
        if (event.getType() == ServiceEvent.REGISTERED) {
            System.out.println("Test Server : Service of type " + objectClass[0] + " registered.");
        }  else if (event.getType() == ServiceEvent.UNREGISTERING) {
            System.out.println("Test Server : Service of type " + objectClass[0] + " unregistered.");
        } else if (event.getType() == ServiceEvent.MODIFIED) {
            System.out.println("Test Server: Service of type " + objectClass[0] + " modified.");
        }
	}

	public static void main(String[] args) throws IOException {
		start();
	}
}
