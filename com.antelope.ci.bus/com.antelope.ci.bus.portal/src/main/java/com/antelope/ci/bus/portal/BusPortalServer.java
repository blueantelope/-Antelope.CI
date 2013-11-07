// com.antelope.ci.bus.portal.BusPortal.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal;

import java.util.Properties;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.shell.BusPortalShellCommand;
import com.antelope.ci.bus.server.BusServer;
import com.antelope.ci.bus.server.BusServerCondition;
import com.antelope.ci.bus.server.BusServerConfig;
import com.antelope.ci.bus.server.service.impl.PasswordAuthServiceImpl;
import com.antelope.ci.bus.server.service.impl.PublickeyAuthServiceImpl;


/**
 * portal server extend to server
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-29		下午9:04:32 
 */
public class BusPortalServer extends BusServer {

	public BusPortalServer() throws CIBusException {
		super();
	}

	@Override
	protected BusServerConfig readConfig() throws CIBusException {
		Properties props = BusPortalActivator.getProperties();
		BusServerConfig config = BusServerConfig.fromProps(props);
		return config;
	}

	@Override
	protected BusServerCondition attatchCondition() throws CIBusException {
		BusServerCondition condition = new BusServerCondition();
		condition.setCommand_class(BusPortalShellCommand.class);
		condition.addAuthService(new PasswordAuthServiceImpl(condition.getUserMap()));
		condition.addAuthService(new PublickeyAuthServiceImpl(condition.getUserMap()));
		return condition;
	}

}

