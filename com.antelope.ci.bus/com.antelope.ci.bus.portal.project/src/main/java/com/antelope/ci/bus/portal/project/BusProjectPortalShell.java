// com.antelope.ci.bus.portal.project.BusProjectPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.PortalConfiguration;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.PortalBlock;
import com.antelope.ci.bus.portal.project.command.ProjectAddHit;
import com.antelope.ci.bus.portal.project.command.ProjectRunMainPortalHit;
import com.antelope.ci.bus.server.shell.Shell;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-27		下午6:03:13 
 */
@Shell(
		name="portal.project",
		commandAdapter="com.antelope.ci.bus.portal.core.shell.command.PortalCommandAdapter",
		status=BusProjectShellStatus.PROJECT)
@PortalConfiguration(
		xml="classpath:/com/antelope/ci/bus/portal/project/portal_project",
		properties="classpath:com.antelope.ci.bus.portal.project.portal_project")
public class BusProjectPortalShell extends BusPortalShell {

	public BusProjectPortalShell() throws CIBusException {
		super();
	}

	@Override
	protected void customInit() throws CIBusException {
		
	}

	@Override
	protected void custom() throws CIBusException {
		commandAdapter.addCommand(new ProjectAddHit());		
		commandAdapter.addCommand(new ProjectRunMainPortalHit());		
	}

	@Override
	protected void shutdown() throws CIBusException {
		
	}

	@Override
	protected PortalBlock loadBlock() {
		PortalBlock block = new PortalBlock();
	
		return block;
	}

}

