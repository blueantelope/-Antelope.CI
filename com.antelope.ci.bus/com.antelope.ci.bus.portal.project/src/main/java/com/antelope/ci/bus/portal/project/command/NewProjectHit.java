// com.antelope.ci.bus.portal.project.command.NewProjectHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project.command;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.command.MainCommonPortalHit;
import com.antelope.ci.bus.portal.project.BusProjectShellStatus;
import com.antelope.ci.bus.server.shell.BusShellMode;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandType;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-27		下午6:10:08 
 */
@Command(
		name="new_project", 
		commands="n, N", 
		status=BusProjectShellStatus.PROJECT, 
		type=CommandType.HIT, 
		mode=BusShellMode.MAIN,
		beforeClear=false, 
		form="classpath:/com/antelope/ci/bus/portal/project/form/new_project.xml",
		property="com.antelope.ci.bus.portal.project.form.project_form")
public class NewProjectHit extends MainCommonPortalHit {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.portal.core.shell.command.MainCommonPortalHit#executeOnMain(com.antelope.ci.bus.portal.core.shell.BusPortalShell, java.lang.Object[])
	 */
	@Override protected String executeOnMain(BusPortalShell shell, Object... args) {
		try {
			shell.enterEdit();
			drawForm(shell);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		return BusProjectShellStatus.PROJECT;
	}

}

