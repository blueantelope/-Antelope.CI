// com.antelope.ci.bus.server.BusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Command;

/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-14		下午12:43:31 
 */
public class BusSshFactory implements Factory<Command> {
	private BusSshCommand command;
	
	public BusSshFactory(BusSshCommand command) {
		this.command = command;
	}

	@Override
	public Command create() {
		return command;
	}
	
}
