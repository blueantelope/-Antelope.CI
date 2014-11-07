// com.antelope.ci.bus.portal.project.command.ProjectAddDownHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project.command;

import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.command.PortalHit;
import com.antelope.ci.bus.portal.project.BusProjectShellMode;
import com.antelope.ci.bus.portal.project.BusProjectShellStatus;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandHelper;
import com.antelope.ci.bus.server.shell.command.CommandType;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月3日		下午12:14:05 
 */
@Command(
		name="project_add_down", 
		commands=CommandHelper.downCommand,
		status=BusProjectShellStatus.PROJECT, 
		type=CommandType.HIT, 
		mode=BusProjectShellMode.PROJECT_FORM_ADD,
		beforeClear=false)
public class ProjectAddDownHit extends PortalHit {
	@Override protected String executeOnMain(BusPortalShell shell, Object... args) {
		downInForm(shell);
		return BusProjectShellStatus.PROJECT;
	}
}

