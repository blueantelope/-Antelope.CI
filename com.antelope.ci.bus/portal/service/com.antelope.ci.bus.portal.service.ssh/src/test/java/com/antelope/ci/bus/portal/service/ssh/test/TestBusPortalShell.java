// com.antelope.ci.bus.portal.test.TestBusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.service.ssh.test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.PortalConfiguration;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.PortalBlock;
import com.antelope.ci.bus.server.shell.Shell;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-8		下午2:13:43 
 */
@Shell(
		name="portal.test", 
		commandAdapter="com.antelope.ci.bus.server.shell.command.hit.HitAdapter")
@PortalConfiguration(
		xml="classpath:/com/antelope/ci/bus/portal/test/portal_test", 
		properties="classpath:com.antelope.ci.bus.portal.test.portal_test")
public class TestBusPortalShell extends BusPortalShell {
	public TestBusPortalShell() throws CIBusException {
		super();
	}

	@Override
	protected void custom() throws CIBusException {
		
	}

	@Override
	protected void customInit() throws CIBusException {
		
	}

	@Override
	protected PortalBlock loadBlock() {
		return null;
	}

	@Override
	protected void customShutdown() throws CIBusException {
		
	}
}