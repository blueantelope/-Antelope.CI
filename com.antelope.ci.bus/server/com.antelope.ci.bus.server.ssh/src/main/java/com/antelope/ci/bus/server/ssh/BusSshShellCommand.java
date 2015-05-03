// com.antelope.ci.bus.server.ssh.BusSshSshCommand.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.BusServerCondition.SERVER_TYPE;
import com.antelope.ci.bus.server.common.BusLauncher;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月2日		下午8:34:44 
 */
public class BusSshShellCommand extends BusSshCommand {
	protected BusLauncher launcher;
	
	public BusSshShellCommand(BusLauncher launcher) {
		super(launcher);
	}

	@Override
	protected SERVER_TYPE customizeType() {
		return SERVER_TYPE.SHELL;
	}

	@Override
	protected void cumstomizeContext() throws CIBusException {
		// nothing
	}
}
