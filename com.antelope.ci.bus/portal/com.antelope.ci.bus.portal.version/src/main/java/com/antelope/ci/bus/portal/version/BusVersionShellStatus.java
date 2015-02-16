// com.antelope.ci.bus.portal.version.BusVersionShellStatus.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.version;

import com.antelope.ci.bus.server.shell.base.ShellStatus;
import com.antelope.ci.bus.server.shell.base.ShellStatusClass;

import com.antelope.ci.bus.server.shell.base.ShellStatus;
import com.antelope.ci.bus.server.shell.base.ShellStatusClass;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-26		下午4:28:36 
 */
@ShellStatusClass
public class BusVersionShellStatus {
	@ShellStatus(code=10, name="command.status.version")
	public static final String VERSION = "command.status.version";
}

