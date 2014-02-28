// com.antelope.ci.bus.portal.project.BusProjectPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.shell.BusPortalShell;
import com.antelope.ci.bus.server.shell.Shell;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-27		下午6:03:13 
 */
@Shell(commandAdapter="com.antelope.ci.bus.server.shell.command.echo.EchoAdapter")
public class BusProjectPortalShell extends BusPortalShell {

	public BusProjectPortalShell() throws CIBusException {
		super();
	}

	@Override
	protected void init() throws CIBusException {
		
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

