// com.antelope.ci.bus.portal.shell.BusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.shell;

import java.io.IOException;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.BusShellSession;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-29		下午9:15:32 
 */
public class BusPortalShell extends BusShell {
	private BusPortalConfigurationHelper configurationHelper;

	public BusPortalShell(BusShellSession session) {
		super(session);
	}

	@Override
	protected void custom() throws CIBusException {
		configurationHelper = BusPortalConfigurationHelper.getHelper();
		configurationHelper.parseXml();
	}

	@Override
	protected void show() throws CIBusException {
		showBanner();
		
	}

	@Override
	protected void shutdown() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}
	
	private void showBanner() {
		try {
			io.println(configurationHelper.getConfiguration().getBanner().getText());
		} catch (IOException e) {
		}
	}

}

