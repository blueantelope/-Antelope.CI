// com.antelope.ci.bus.portal.dashboard.BusDashboardShellStatus.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.dashboard;

import com.antelope.ci.bus.server.shell.base.ShellStatus;
import com.antelope.ci.bus.server.shell.base.ShellStatusClass;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-26		下午3:09:00 
 */
@ShellStatusClass
public class BusDashboardShellStatus {
	@ShellStatus(code=6, name="command.status.dashboard")
	public static final String DAHSBOARD = "command.status.dashboard";
}

