// com.antelope.ci.bus.portal.project.BusProjectShellStatus.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project;

import com.antelope.ci.bus.server.shell.Status;
import com.antelope.ci.bus.server.shell.StatusClass;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-27		下午6:40:23 
 */
@StatusClass
public class BusProjectShellStatus {
	@Status(code=6, name="command.status.project")
	public static final String PROJECT 			= "command.status.project";
}

