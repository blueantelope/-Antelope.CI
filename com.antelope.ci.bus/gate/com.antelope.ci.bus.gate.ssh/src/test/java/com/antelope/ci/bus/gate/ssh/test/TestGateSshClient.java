// com.antelope.ci.bus.gate.ssh.test.TestClient.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.ssh.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import junit.framework.TestCase;

import org.apache.sshd.ClientChannel;
import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;
import org.junit.Test;

import com.antelope.ci.bus.server.api.message.ApiMessage;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月23日		下午5:20:52 
 */
public class TestGateSshClient extends TestCase {
	private final static String HOST = "localhost";
	private final static int PORT = 9428;
	private final static String USERNAME = "blueantelope";
	private final static String PASSWORD = "blueantelope";
	private final static long CLOSE_TIMEOUT = 0;
	
	@Test public void test() throws Exception {
		SshClient client = SshClient.setUpDefaultClient();
		client.start();
        ClientSession session = client.connect(HOST, PORT).await().getSession();
        session.authPassword(USERNAME, PASSWORD).await().isSuccess();
        ClientChannel channel = session.createChannel(ClientChannel.CHANNEL_SHELL);
        
        ByteArrayOutputStream sent = new ByteArrayOutputStream();
        PipedOutputStream pipedIn = new TeePipedOutputStream(sent);
        channel.setIn(new PipedInputStream(pipedIn));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        channel.setOut(out);
        channel.setErr(err);
        channel.open();
        
        pipedIn.write(new ApiMessage().getBytes());
        pipedIn.flush();
        
        channel.waitFor(ClientChannel.CLOSED, CLOSE_TIMEOUT);
        channel.close(true);
        client.stop();
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

	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestGateSshClient.class);
	}
}
