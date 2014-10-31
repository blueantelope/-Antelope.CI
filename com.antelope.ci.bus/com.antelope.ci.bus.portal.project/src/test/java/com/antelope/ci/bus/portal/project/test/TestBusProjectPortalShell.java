// com.antelope.ci.bus.portal.project.test.TestBusProjectPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project.test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.PortalConfiguration;
import com.antelope.ci.bus.portal.project.BusProjectPortalShell;
import com.antelope.ci.bus.portal.project.BusProjectShellMode;
import com.antelope.ci.bus.portal.project.BusProjectShellStatus;
import com.antelope.ci.bus.server.shell.BusShellMode;
import com.antelope.ci.bus.server.shell.Shell;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年10月31日		上午11:25:20 
 */
@Shell(
		name="portal.project",
		commandAdapter="com.antelope.ci.bus.portal.core.shell.command.PortalCommandAdapter",
		status=BusProjectShellStatus.PROJECT)
@PortalConfiguration(
		xml="classpath:/com/antelope/ci/bus/portal/project/portal_project",
		properties="classpath:com.antelope.ci.bus.portal.project.portal_project")
public class TestBusProjectPortalShell extends BusProjectPortalShell {

	public TestBusProjectPortalShell() throws CIBusException {
		super();
	}

	
	@Override
	protected void customInit() throws CIBusException {
		BusShellMode.addModeClass(BusProjectShellMode.class);
	}
}

