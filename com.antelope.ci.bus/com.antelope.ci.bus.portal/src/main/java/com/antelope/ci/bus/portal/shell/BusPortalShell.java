// com.antelope.ci.bus.portal.shell.BusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.shell;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.server.shell.BusBaseFrameShell;

/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-29		下午9:15:32 
 */
public class BusPortalShell extends BusBaseFrameShell {
	private static final Logger log = Logger.getLogger(BusPortalShell.class);
	private BusPortalConfigurationHelper configurationHelper;

	public BusPortalShell() {
		super();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#custom()
	 */
	@Override
	protected void custom() throws CIBusException {
		configurationHelper = BusPortalConfigurationHelper.getHelper();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#shutdown()
	 */
	@Override
	protected void shutdown() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}
	
	private void showBanner() {
		String banner = configurationHelper.getConfiguration().getBanner().getText();
		if (!StringUtil.empty(banner))
			println(banner);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusBaseFrameShell#view()
	 */
	@Override
	protected void view() throws CIBusException {
		showBanner();
	}
}

