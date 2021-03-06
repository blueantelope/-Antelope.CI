// com.antelope.ci.bus.portal.usecase.BusUsecasePortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.usecase;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.PortalConfiguration;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.PortalBlock;
import com.antelope.ci.bus.server.shell.base.Shell;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-26		下午4:18:15 
 */
@Shell(
		name="portal.usecase", 
		commandAdapter="com.antelope.ci.bus.portal.core.shell.command.PortalCommandAdapter", 
		status=BusUsecaseShellStatus.USECASE)
@PortalConfiguration(
		xml="classpath:/com/antelope/ci/bus/portal/usecase/portal_usecase", 
		properties="classpath:com.antelope.ci.bus.portal.usecase.portal_usecase")
public class BusUsecasePortalShell extends BusPortalShell {

	public BusUsecasePortalShell() throws CIBusException {
		super();
	}

	@Override
	protected void customInit() throws CIBusException {
		
	}

	@Override
	protected void customShellEnv() throws CIBusException {
		
	}

	@Override
	protected PortalBlock loadBlock() {
		return null;
	}

	@Override
	protected void customShutdown() throws CIBusException {
		
	}
}