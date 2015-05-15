// com.antelope.ci.bus.portal.ssh.BusPortalSshComplexActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.ssh;

import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusComplexActivator;
import com.antelope.ci.bus.osgi.BusOsgiUtil;
import com.antelope.ci.bus.portal.BusPortalContext;
import com.antelope.ci.bus.portal.core.service.ConfigurationService;
import com.antelope.ci.bus.portal.core.service.ShellService;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月15日		下午5:25:44 
 */
public class BusPortalSshComplexActivator extends BusComplexActivator{
	private BusPortalSshServer sshServer;
	
	public BusPortalSshComplexActivator() {
		super(BusPortalSshContext.CONTEXT_CLAZZ);
	}

	@Override
	protected void customInit() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void run() throws CIBusException {
		if (sshServer == null) {
			sshServer = new BusPortalSshServer((BusPortalSshContext) context);
			Object shellService = fetchService(ShellService.NAME);
			if (shellService != null)
				sshServer.initLauncher(((ShellService)shellService).getManager().getContainerLauncher()) ;
			BusOsgiUtil.publishService(bundle_context, sshServer, BusPortalSshServer.NAME);
			Object service = fetchService(ConfigurationService.NAME);
			if (service != null)
				sshServer.setWaitForStart(((ConfigurationService)service).getStartWait()) ;
			else
				sshServer.setWaitForStart(BusPortalContext.DEF_START_WAIT);
			System.out.println("*********************** @antelope.ci ssh portal server start, wait a moment... ***********************");
			sshServer.open();
			System.out.println("*********************** @antelope.ci ssh portal server finish stsart, enjoy it! ***********************");
		}
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

