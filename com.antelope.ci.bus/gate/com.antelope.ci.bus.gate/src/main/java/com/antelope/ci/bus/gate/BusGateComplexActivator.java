// com.antelope.ci.bus.gate.BusGateComplexActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate;

import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.gate.api.BusGateApi;
import com.antelope.ci.bus.gate.api.service.GateService;
import com.antelope.ci.bus.osgi.BusComplexActivator;
import com.antelope.ci.bus.osgi.BusOsgiUtil;
import com.antelope.ci.bus.osgi.ServicePublisher;
import com.antelope.ci.bus.server.api.launcher.BusApiCondition;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月14日		下午5:28:14 
 */
public class BusGateComplexActivator extends BusComplexActivator {
	protected GateService gateService;
	protected BusApiCondition condition;
	private GateApiScanner sanner;

	@Override
	protected void customInit() throws CIBusException {
		condition = new BusApiCondition(classLoader());
		condition.setApiClass(BusGateApi.class.getName());
	}

	@Override
	protected void run() throws CIBusException {
		final GateApiScanner sanner = GateApiScanner.getScanner();
		new Thread() {
			@Override public void run() {
				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e) { }
				sanner.setServiceMap(context.getServiceMap());
			}
		}.start();
		sanner.setClassLoader(classLoader());
		sanner.start();
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
		if (gateService == null) {
			gateService = new GateService(condition);
			BusOsgiUtil.publishService(bundle_context, gateService, GateService.NAME);
		}
		
		ServicePublisher.publish(bundle_context, "com.antelope.ci.bus.gate.service");
	}

	@Override
	protected void removeServices() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

}

