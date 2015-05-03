// com.antelope.ci.bus.server.ssh.BusSshCommandFactory.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh;

import org.apache.log4j.Logger;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.CommandFactory;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月2日		下午7:49:31 
 */
public class BusSshApiFactory implements CommandFactory {
	private static final Logger log = Logger.getLogger(BusSshApiFactory.class);
	private BusSshApiCommand command;
	
	public BusSshApiFactory(BusSshApiCommand command) {
		super();
		this.command = command;
	}

	@Override
	public Command createCommand(String input) {
		command.setInput(input);
		return command;
	}
}
