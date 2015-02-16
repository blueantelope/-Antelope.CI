// com.antelope.ci.bus.portal.project.command.ProjectRunMainPortalHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project.command;

import com.antelope.ci.bus.portal.core.shell.command.RunMainPortalHit;
import com.antelope.ci.bus.portal.project.BusProjectShellStatus;
import com.antelope.ci.bus.server.shell.base.BusShellMode;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandHelper;
import com.antelope.ci.bus.server.shell.command.CommandType;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年9月29日		下午6:26:24 
 */
@Command(
		name="run.project", 
		commands=CommandHelper.enterCommand, 
		status=BusProjectShellStatus.PROJECT, 
		type=CommandType.HIT, 
		mode=BusShellMode.MAIN,
		beforeClear=false, 
		form="classpath:/com/antelope/ci/bus/portal/project/form/project_add.xml",
		property="com.antelope.ci.bus.portal.project.form.project_form")
public class ProjectRunMainPortalHit extends RunMainPortalHit {

}