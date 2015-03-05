// com.antelope.ci.bus.gate.ssh.BusGateSshServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.ssh;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.BusServerCondition;
import com.antelope.ci.bus.server.BusServerConfig;
import com.antelope.ci.bus.server.ssh.BusSshServer;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月19日		下午1:08:07 
 */
public class BusGateSshServer extends BusSshServer {
	private static final Logger log = Logger.getLogger(BusGateSshServer.class);
	public static final String NAME = "com.antelope.ci.bus.gate.ssh.BusGateSshServer";
	
	public BusGateSshServer(BundleContext bundle_context) throws CIBusException {
		super(bundle_context);
	}

	@Override
	protected void beforeRun() throws CIBusException {
		
	}

	@Override
	protected void afterRun() throws CIBusException {
		
	}

	@Override
	protected void customizeConfig(BusServerConfig config)
			throws CIBusException {
		
	}

	@Override
	protected void attatchCondition(BusServerCondition server_condition)
			throws CIBusException {
		
	}

	@Override
	protected void customizeInit() throws CIBusException {
		
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.ssh.BusSshServer#toSummary()
	 */
	@Override
	public String toSummary() {
		return "Gate SSH Server";
	}
}
