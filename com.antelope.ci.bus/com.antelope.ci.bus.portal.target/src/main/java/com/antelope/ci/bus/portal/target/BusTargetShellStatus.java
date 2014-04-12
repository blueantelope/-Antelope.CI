// com.antelope.ci.bus.portal.target.BusTargetShellStatus.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.target;

import com.antelope.ci.bus.server.shell.Status;
import com.antelope.ci.bus.server.shell.StatusClass;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-12		下午9:50:01 
 */
@StatusClass
public class BusTargetShellStatus {
	@Status(code=8, name="command.status.target")
	public static final String TARGET			= "command.status.target";
}

