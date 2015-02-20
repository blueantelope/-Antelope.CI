// com.antelope.ci.bus.gate.ssh.BusGateActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.ssh;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.BusServerTemplateActivator;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月19日		下午1:13:08 
 */
public class BusGateSshActivator extends BusServerTemplateActivator {
	private BusGateSshServer sshServer; 
	
	@Override
	protected void run() throws CIBusException {
		if (sshServer == null) {
			sshServer = new BusGateSshServer(bundle_context);
			System.out.println("*********************** @antelope.ci ssh gate server start, wait a moment... ***********************");
			sshServer.open();
			System.out.println("*********************** @antelope.ci ssh gate server finish stsart, enjoy it! ***********************");
		}
	}

	@Override
	protected void destroy() throws CIBusException {
		
	}

	@Override
	protected void handleLoadService(String clsName, ServiceReference ref,
			Object service) throws CIBusException {
		
	}

	@Override
	protected void handleUnloadService(ServiceReference ref)
			throws CIBusException {
		
	}

	@Override
	protected void handleStopAllService() throws CIBusException {
		
	}

	@Override
	protected void publishServices() throws CIBusException {
		
	}

	@Override
	protected void removeServices() throws CIBusException {
		
	}

	@Override
	protected String[] customLoadServices() {
		return null;
	}

	@Override
	protected void customInit() throws CIBusException {
		
	}
}
