// com.antelope.ci.bus.portal.BusPortal.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal;

import java.util.Properties;

import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusOsgiUtil;
import com.antelope.ci.bus.portal.core.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.portal.core.shell.SimpleBusPortalShell;
import com.antelope.ci.bus.server.BusServer;
import com.antelope.ci.bus.server.BusServerCondition;
import com.antelope.ci.bus.server.BusServerCondition.LAUNCHER_TYPE;
import com.antelope.ci.bus.server.BusServerConfig;


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
	
	public BusPortalServer(BundleContext m_context) throws CIBusException {
		super(m_context);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusServer#readConfig()
	 */
	@Override
	protected BusServerConfig readConfig() throws CIBusException {
		Properties props = BusPortalActivator.getProperties();
		BusServerConfig config = BusServerConfig.fromProps(props);
		return config;
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
	 * @see com.antelope.ci.bus.server.BusServer#customInit()
	 */
	@Override
	protected void customInit() throws CIBusException {
		BusPortalConfigurationHelper configurationHelper = BusPortalConfigurationHelper.getHelper();
		configurationHelper.setClassLoader(BusOsgiUtil.getBundleClassLoader(m_context));
		configurationHelper.init();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusServer#customRun()
	 */
	@Override
	protected void customRun() throws CIBusException {
		
	}
}

