// com.antelope.ci.bus.portal.BusPortal.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.service.ssh;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.PropertiesUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusOsgiUtil;
import com.antelope.ci.bus.osgi.BusPropertiesHelper;
import com.antelope.ci.bus.osgi.BusActivator;
import com.antelope.ci.bus.portal.core.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.portal.core.entrance.EntranceManager;
import com.antelope.ci.bus.portal.core.shell.SimpleBusPortalShell;
import com.antelope.ci.bus.server.BusServerCondition;
import com.antelope.ci.bus.server.BusServerCondition.LAUNCHER_TYPE;
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
	protected final static String START_WAIT = "bus.portal.start.wait";
	protected final static long DEF_START_WAIT = 0;
	
	public BusPortalSshServer() throws CIBusException {
		super();
	}
	
	public BusPortalSshServer(BundleContext m_context) throws CIBusException {
		super(m_context);
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
		server_condition.setLauncherType(LAUNCHER_TYPE.CONTAINER);
		if (server_condition.isShellEmpty())
			server_condition.addDefaultShellClass(SimpleBusPortalShell.class.getName());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusServer#customizeInit()
	 */
	@Override
	protected void customizeInit() throws CIBusException {
		BusPropertiesHelper helper = new BusPropertiesHelper(BusActivator.getContext());
		waitForStart = PropertiesUtil.getLong(helper.getAll(), START_WAIT, DEF_START_WAIT);
		
		BusPortalConfigurationHelper configurationHelper = BusPortalConfigurationHelper.getHelper();
		configurationHelper.setClassLoader(BusOsgiUtil.getBundleClassLoader(m_context));
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
		EntranceManager.monitor(m_context, condition);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
	}
}

