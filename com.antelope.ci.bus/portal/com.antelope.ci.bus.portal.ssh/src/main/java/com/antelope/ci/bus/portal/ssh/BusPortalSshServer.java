// com.antelope.ci.bus.portal.BusPortal.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.ssh;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.server.BusServerCondition;
import com.antelope.ci.bus.server.BusServerConfig;
import com.antelope.ci.bus.server.ssh.BusSshServer;

/**
 * portal server extend to server
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-29		下午9:04:32 
 */
public class BusPortalSshServer extends BusSshServer {
	private static final Logger log = Logger.getLogger(BusPortalSshServer.class);
	public static final String NAME = "com.antelope.ci.bus.portal.ssh.BusPortalSshServer";
	
	public BusPortalSshServer() throws CIBusException {
		super();
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusServer#customizeConfig(com.antelope.ci.bus.server.BusServerConfig)
	 */
	@Override
	protected void customizeConfig(BusServerConfig config) throws CIBusException {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusServer#attatchCondition(com.antelope.ci.bus.server.BusServerCondition)
	 */
	@Override
	protected void attatchCondition(BusServerCondition server_condition) throws CIBusException {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusServer#customizeInit()
	 */
	@Override
	protected void customizeInit() throws CIBusException {
		BusPortalConfigurationHelper configurationHelper = BusPortalConfigurationHelper.getHelper();
		configurationHelper.init();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.ssh.BusSshServer#beforeRun()
	 */
	@Override
	protected void beforeRun() throws CIBusException {
		log.info("before run portal");
	}
	
	protected void afterRun() throws CIBusException {
		log.info("after run portal");
	}
}

