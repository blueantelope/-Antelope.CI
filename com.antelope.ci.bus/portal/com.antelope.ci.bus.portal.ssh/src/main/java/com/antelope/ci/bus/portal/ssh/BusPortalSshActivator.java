// com.antelope.ci.bus.portal.ssh.BusPortalSshActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.ssh;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusOsgiUtil;
import com.antelope.ci.bus.portal.BusPortalActivator;
import com.antelope.ci.bus.portal.core.service.ConfigurationService;
import com.antelope.ci.bus.portal.core.service.ShellService;
import com.antelope.ci.bus.server.BusServerTemplateActivator;

/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月9日		下午1:53:55 
 */
public class BusPortalSshActivator extends BusServerTemplateActivator {
	private BusPortalSshServer sshServer; 
	
	@Override
	protected void run() throws CIBusException {
		if (sshServer == null) {
			sshServer = new BusPortalSshServer(bundle_context);
			Object shellService = fetchService(ShellService.NAME);
			if (shellService != null)
				sshServer.initShellLauncher(((ShellService)shellService).getManager().getContainerLauncher()) ;
			BusOsgiUtil.publishService(bundle_context, sshServer, BusPortalSshServer.NAME);
			Object service = fetchService(ConfigurationService.NAME);
			if (service != null)
				sshServer.setWaitForStart(((ConfigurationService)service).getStartWait()) ;
			else
				sshServer.setWaitForStart(BusPortalActivator.DEF_START_WAIT);
			System.out.println("*********************** @antelope.ci ssh portal server start, wait a moment... ***********************");
			sshServer.open();
			System.out.println("*********************** @antelope.ci ssh portal server finish stsart, enjoy it! ***********************");
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

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.BusActivator#publishServices()
	 */
	@Override
	protected void publishServices() throws CIBusException {
		
	}

	@Override
	protected void removeServices() throws CIBusException {
		
	}

	@Override
	protected String[] customLoadServices() {
		return new String[] {
				ShellService.NAME,
				ConfigurationService.NAME
		};
	}

	@Override
	protected void customInit() throws CIBusException {
		
	}
}
