// com.antelope.ci.bus.gate.shell.ssh.BusGateShellSshComplexActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.shell.ssh;

import org.apache.log4j.Logger;
import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.gate.service.GateShellService;
import com.antelope.ci.bus.osgi.BusComplexActivator;
import com.antelope.ci.bus.osgi.BusOsgiUtil;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月15日		下午5:04:43 
 */
public class BusGateShellSshComplexActivator extends BusComplexActivator {
	private static final Logger log = Logger.getLogger(BusGateShellSshComplexActivator.class);
	private BusGateShellSshServer sshServer; 
	
	public BusGateShellSshComplexActivator() {
		super(BusGateShellSshContext.CONTEXT_CLAZZ);
	}

	@Override
	protected void customInit() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void run() throws CIBusException {
		new Thread() {
			@Override public void run() {
				try {
					try {
						Thread.sleep(5 * 1000);
					} catch (InterruptedException e) { }
					if (sshServer == null) {
						sshServer = new BusGateShellSshServer((BusGateShellSshContext) context);
						Object shellService = fetchService(GateShellService.NAME);
						if (shellService != null)
							sshServer.initLauncher(((GateShellService)shellService).getManager().getProxyLauncher());
						BusOsgiUtil.publishService(bundle_context, sshServer, BusGateShellSshServer.NAME);
						System.out.println("*********************** @antelope.ci ssh gate shell server start, wait a moment... ***********************");
						sshServer.open();
						System.out.println("*********************** @antelope.ci ssh gate shell server finish, enjoy it! ***********************");
					}
				} catch (CIBusException e) {
					log.error("ERROR - start gate shell server\n" + e);
				}
			}
		}.start();
	}

	@Override
	protected void destroy() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleLoadService(String clsName, ServiceReference ref,
			Object service) throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleUnloadService(ServiceReference ref)
			throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleStopAllService() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addServices() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeServices() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

}
