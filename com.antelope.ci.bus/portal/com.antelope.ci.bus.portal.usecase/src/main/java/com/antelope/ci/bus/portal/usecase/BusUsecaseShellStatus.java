package com.antelope.ci.bus.portal.usecase;

import com.antelope.ci.bus.server.shell.base.ShellStatus;
import com.antelope.ci.bus.server.shell.base.ShellStatusClass;

// .BusUsecaseShellStatus.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
 */

/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-26		下午4:19:25 
 */
@ShellStatusClass
public class BusUsecaseShellStatus {
	@ShellStatus(code=8, name="command.status.usecase")
	public static final String USECASE = "command.status.usecase";
}

