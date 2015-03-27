// com.antelope.ci.bus.gate.ssh.test.CommonGateSshClient.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.ssh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import junit.framework.TestCase;

import org.apache.sshd.ClientChannel;
import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月27日		下午12:07:51 
 */
public abstract class CommonGateSshClient extends TestCase {
	protected String host;
	protected int port;
	protected String username;
	protected String password;
	protected long close_timeout;
	protected SshClient client;
	protected ClientChannel channel;
	protected PipedOutputStream inSender;
	protected PipedInputStream in; 
	protected ByteArrayOutputStream out;
	protected ByteArrayOutputStream err;
	
	@Before public void setUp() throws Exception {
		host = "localhost";
		port = 9428;
		username = "blueantelope";
		password = "blueantelope";
		close_timeout = 1 * 1000;
		
		client = SshClient.setUpDefaultClient();
		client.start();
        ClientSession session = client.connect(host, port).await().getSession();
        session.authPassword(username, password).await().isSuccess();
        channel = session.createChannel(ClientChannel.CHANNEL_SHELL);
        
        inSender = new TeePipedOutputStream(new ByteArrayOutputStream());
        in = new PipedInputStream(inSender);
        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
        channel.setIn(in);
        channel.setOut(out);
        channel.setErr(err);
        channel.open();
	}
	
	@Test public void test() throws Exception {
		testRun();
	}
	
	@After protected void tearDown() {
	    channel.waitFor(ClientChannel.CLOSED, close_timeout);
        channel.close(true);
        client.stop();
	}
	
	protected void noClose() {
		close_timeout = 0;
	}
	
	private static class TeePipedOutputStream extends PipedOutputStream {
	    private OutputStream tee;
	    public TeePipedOutputStream(OutputStream tee) {
	        this.tee = tee;
	    }
	    @Override
	    public void write(int b) throws IOException {
	        super.write(b);
	        tee.write(b);
	    }
	    @Override
	    public void write(byte[] b, int off, int len) throws IOException {
	        super.write(b, off, len);
	        tee.write(b, off, len);
	    }
	}
	
	public abstract void testRun() throws Exception;
}

