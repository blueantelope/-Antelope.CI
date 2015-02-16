// com.antelope.ci.bus.portal.shell.BusDefaultPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-28		下午6:14:27 
 */
public class SimpleBusPortalShell extends BusPortalShell {
	
	public SimpleBusPortalShell() throws CIBusException {
		super();
	}

	@Override
	protected void customInit() throws CIBusException {
		
	}

	@Override
	protected void custom() throws CIBusException {
		
	}

	@Override
	protected void customShutdown() throws CIBusException {
		
	}

	@Override
	protected PortalBlock loadBlock() {
		return null;
	}
}
