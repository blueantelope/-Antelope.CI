// com.antelope.ci.bus.gate.api.client.SshClient.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.connector;

import java.io.ByteArrayOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.sshd.ClientChannel;
import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月15日		下午3:51:09 
 */
@Connector(name="ssh")
public class SshConnector implements IConnector {
	protected SshAttribute attribute;
	protected SshClient client;
	protected ClientChannel channel;
	protected byte[] buffer;
	
	public SshConnector(SshAttribute attribute) {
		super();
		this.attribute = attribute;
		buffer = new byte[attribute.getBufsize()];
	}
	
	@Override
	public void connect() throws CIBusException {
		try {
			client = SshClient.setUpDefaultClient();
			client.start();
	        ClientSession session = client.connect(attribute.getHost(), attribute.getPort()).await().getSession();
	        session.authPassword(attribute.getUsername(), attribute.getPassword()).await().isSuccess();
	        channel = session.createChannel(ClientChannel.CHANNEL_SHELL);
	        channel.setIn(attribute.getIn());
	        channel.setOut(attribute.getOut());
	        channel.setErr(attribute.getErr());
	        channel.open();
		} catch (Exception e) {
			throw new CIBusException("", "error connect with ssh", e);
		}
	}
	
	@Override
	public byte[] read() throws CIBusException {
		try {
			PipedInputStream in = attribute.getIn();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			int len = -1;
			while ((len=in.read(buffer)) != -1) {
				output.write(buffer, 0, len);
				output.flush();
				if (in.available() > 0)
					continue;
				break;
			}
			return output.toByteArray();
		} catch (Exception e) {
			throw new CIBusException("", "error read datas with ssh", e);
		}
	}

	@Override
	public void send(byte[] datas) throws CIBusException {
		try {
			PipedOutputStream sender = attribute.getSender();
			sender.write(datas);
			sender.flush();
		} catch (Exception e) {
			throw new CIBusException("", "error send datas with ssh", e);
		}
	}
	
	@Override
	public void close() throws CIBusException {
		if (channel != null) {
			if (attribute.getWaitForClose() > 0) {
				channel.waitFor(ClientChannel.CLOSED, attribute.getWaitForClose());
				channel.close(false);
			} else {
				channel.waitFor(ClientChannel.CLOSED, 0);
				channel.close(true);
			}
        	client.stop();
		}
	}
}
