// com.antelope.ci.bus.portal.dashboard.BusDashBoardPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.dashboard;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.PortalConfiguration;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.server.shell.Shell;




/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-26		下午3:06:40 
 */
@Shell(name="portal.dashboard", commandAdapter="com.antelope.ci.bus.portal.shell.command.PortalCommandAdapter", status=BusDashboardShellStatus.DAHSBOARD)
@PortalConfiguration(xml="classpath:/com/antelope/ci/bus/portal/dashboard/portal_dashboard", properties="classpath:com.antelope.ci.bus.portal.dashboard.portal_dashboard")
public class BusDashboardPortalShell extends BusPortalShell {

	public BusDashboardPortalShell() throws CIBusException {
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

