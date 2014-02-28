// com.antelope.ci.bus.portal.test.TestBusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.portal.shell.BusPortalShell;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-8		下午2:13:43 
 */
public class TestBusPortalShell extends BusPortalShell {
	
	

	public TestBusPortalShell() throws CIBusException {
		
		super();
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected void custom() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void shutdown() throws CIBusException {

	}

	@Override
	protected void init() throws CIBusException {
		BusPortalConfigurationHelper.getHelper().init();
		this.portal_config = BusPortalConfigurationHelper.getHelper().getPortal();
	}

	
}

