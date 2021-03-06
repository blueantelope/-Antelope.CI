// com.antelope.ci.bus.portal.project.command.ProjectAction.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project.command;

import java.util.Map;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.model.project.Project;
import com.antelope.ci.bus.portal.core.shell.form.CommonFormAction;
import com.antelope.ci.bus.portal.core.shell.form.FormAction;
import com.antelope.ci.bus.portal.core.shell.form.FormCommand;
import com.antelope.ci.bus.portal.project.BusProjectShellMode;
import com.antelope.ci.bus.server.shell.base.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.CommandHelper;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月18日		下午4:19:39 
 */
@FormAction
public class ProjectAction extends CommonFormAction {
	@FormCommand(
		commands=CommandHelper.escCommand+CommandHelper.enterCommand,
		mode=BusProjectShellMode.PROJECT_FORM_ADD
	)
	public String addProject() {
		Map<String, String> contents = shell.getActiveFormContext().getFormContents();
		Project project = new Project();
		try {
			project.fromMap(contents);
			getProjectManager().addProject(project);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		
		return BusShellStatus.KEEP;
	}
}