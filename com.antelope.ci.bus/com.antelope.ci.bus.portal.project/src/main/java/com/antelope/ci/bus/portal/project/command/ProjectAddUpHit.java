// com.antelope.ci.bus.portal.project.command.ProjectAddUpHit.java
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
 * @Date	 2014年10月31日		下午4:59:03 
 */
@Command(
		name="project_add_up", 
		commands=CommandHelper.upCommand,
		status=BusProjectShellStatus.PROJECT, 
		type=CommandType.HIT, 
		mode=BusProjectShellMode.PROJECT_FORM_ADD,
		beforeClear=false)
public class ProjectAddUpHit extends PortalHit {
	@Override protected String executeOnMain(BusPortalShell shell, Object... args) {
		upInForm(shell);
		return BusProjectShellStatus.PROJECT;
	}
}