// com.antelope.ci.bus.server.test.TestSshServerWithKey.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh.test;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.junit.Test;


/**
 * 自带key ssh server
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-7		下午5:28:06 
 */
public class TestSshServerWithKey extends TestCase {
	private static final int port = 9427;
	
	@Test
	public void testRun() throws IOException {
		SshServer sshServer = SshServer.setUpDefaultServer();
		sshServer.setPort(port);
		sshServer.setKeyPairProvider(new FileKeyPairProvider(new String[] {TestSshServerWithKey.class.getResource("/com.antelope.ci.bus.key").getFile()}));
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
	
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestSshServerWithKey.class);
	}

}

