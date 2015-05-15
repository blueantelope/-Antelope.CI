// com.antelope.ci.bus.portal.BusPortalComplexActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal;

import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusComplexActivator;
import com.antelope.ci.bus.osgi.BusOsgiUtil;
import com.antelope.ci.bus.osgi.ServicePublisher;
import com.antelope.ci.bus.portal.core.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.portal.core.configuration.BusPortalFormHelper;
import com.antelope.ci.bus.portal.core.entrance.EntranceManager;
import com.antelope.ci.bus.portal.core.service.ConfigurationService;
import com.antelope.ci.bus.portal.core.service.ShellService;
import com.antelope.ci.bus.server.shell.launcher.BusMultiShellCondition;
import com.antelope.ci.bus.server.shell.launcher.BusShellCondition;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月15日		上午10:10:04 
 */
public class BusPortalComplexActivator extends BusComplexActivator {
	protected BusShellCondition condition;
	protected ShellService shellService;
	
	public BusPortalComplexActivator() {
		super(BusPortalContext.CONTEXT_CLAZZ);
	}

	@Override
	protected void customInit() throws CIBusException {
		condition = new BusMultiShellCondition(new Object[]{context}, classLoader());
		BusPortalConfigurationHelper configurationHelper = BusPortalConfigurationHelper.getHelper();
		configurationHelper.init((BusPortalContext) context);
		BusPortalFormHelper.initClassLoader(classLoader());
	}

	@Override
	protected void run() throws CIBusException {
		EntranceManager.monitor(condition, bundle_context);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
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
		if (shellService == null) {
			shellService = new ShellService(condition);
			BusOsgiUtil.publishService(bundle_context, shellService, ShellService.NAME);
		}
		ConfigurationService configurationService = ((BusPortalContext) context).getConfigurationService();
		BusOsgiUtil.publishService(bundle_context, configurationService, ConfigurationService.NAME);
		ServicePublisher.publish(bundle_context, "com.antelope.ci.bus.portal.service");
	}

	@Override
	protected void removeServices() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}
}

