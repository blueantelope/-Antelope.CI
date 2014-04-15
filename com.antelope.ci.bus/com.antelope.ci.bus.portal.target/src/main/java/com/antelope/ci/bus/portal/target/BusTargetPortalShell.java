// com.antelope.ci.bus.portal.target.BusTargetPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.target;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.configuration.PortalConfiguration;
import com.antelope.ci.bus.portal.shell.BusPortalShell;
import com.antelope.ci.bus.server.shell.Shell;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-12		下午9:51:03 
 */
@Shell(name="portal.target", commandAdapter="com.antelope.ci.bus.portal.shell.command.PortalCommandAdapter", status=BusTargetShellStatus.TARGET)
@PortalConfiguration(xml="classpath:/com/antelope/ci/bus/portal/target/portal_target", properties="classpath:com.antelope.ci.bus.portal.target.portal_target")
public class BusTargetPortalShell extends BusPortalShell {

	public BusTargetPortalShell() throws CIBusException {
		super();
	}

	@Override
	protected void customInit() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void custom() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void shutdown() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

}

