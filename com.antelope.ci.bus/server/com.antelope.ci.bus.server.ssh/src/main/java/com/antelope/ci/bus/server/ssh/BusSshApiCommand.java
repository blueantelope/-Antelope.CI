// com.antelope.ci.bus.server.ssh.BusSshApiCommand.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.apache.sshd.common.channel.ChannelPipedInputStream;
import org.apache.sshd.common.channel.ChannelPipedOutputStream;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.BusServerCondition.SERVER_TYPE;
import com.antelope.ci.bus.server.common.BusLauncher;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月2日		下午8:47:11 
 */
public class BusSshApiCommand extends BusSshCommand {
	private static final Logger log = Logger.getLogger(BusSshApiCommand.class);
	protected String input;
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public BusSshApiCommand(BusLauncher launcher) {
		super(launcher);
	}
	
	@Override
	protected SERVER_TYPE customizeType() {
		return SERVER_TYPE.API;
	}

	@Override
	protected void cumstomizeContext() throws CIBusException {
		try {
			OutputStream commandIn = new ChannelPipedOutputStream((ChannelPipedInputStream) in);
			commandIn.write(input.getBytes());
			commandIn.flush();
		} catch (IOException e) {
			throw new CIBusException("", "", e);
		}
	}
}
