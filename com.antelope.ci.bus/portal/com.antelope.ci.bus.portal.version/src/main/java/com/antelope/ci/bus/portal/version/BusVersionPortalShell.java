// com.antelope.ci.bus.portal.version.BusVersionPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.version;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.PortalConfiguration;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.PortalBlock;
import com.antelope.ci.bus.server.shell.base.Shell;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-26		下午4:28:20 
 */
@Shell(
		name="portal.target", 
		commandAdapter="com.antelope.ci.bus.portal.core.shell.command.PortalCommandAdapter", 
		status=BusVersionShellStatus.VERSION)
@PortalConfiguration(
		xml="classpath:/com/antelope/ci/bus/portal/version/portal_version", 
		properties="classpath:com.antelope.ci.bus.portal.version.portal_version")
public class BusVersionPortalShell extends BusPortalShell {
	public BusVersionPortalShell() throws CIBusException {
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