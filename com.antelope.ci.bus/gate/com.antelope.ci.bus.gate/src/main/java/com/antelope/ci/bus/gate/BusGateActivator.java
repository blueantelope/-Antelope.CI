// com.antelope.ci.bus.gate.BusGateActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate;

import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.gate.core.service.ShellService;
import com.antelope.ci.bus.osgi.BusActivator;
import com.antelope.ci.bus.osgi.BusOsgiUtil;
import com.antelope.ci.bus.osgi.ServicePublisher;
import com.antelope.ci.bus.server.shell.launcher.BusShellCondition;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年12月13日		下午8:00:59 
 */
public class BusGateActivator extends BusActivator {
	protected ShellService shellService;
	protected BusShellCondition condition;

	@Override
	protected void customInit() throws CIBusException {
		condition = new BusShellCondition(getClassLoader());
	}

	@Override
	protected void run() throws CIBusException {
		
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
		if (shellService == null) {
			shellService = new ShellService(condition);
			BusOsgiUtil.publishService(bundle_context, shellService, ShellService.NAME);
		}
		
		ServicePublisher.publish(bundle_context, "com.antelope.ci.bus.gate.service");
	}

	@Override
	protected void removeServices() throws CIBusException {
		
	}

	@Override
	protected String[] customLoadServices() {
		return null;
	}
}
