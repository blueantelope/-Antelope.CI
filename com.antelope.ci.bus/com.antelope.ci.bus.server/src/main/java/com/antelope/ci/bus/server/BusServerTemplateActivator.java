// com.antelope.ci.bus.server.BusServerTemplateActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusActivator;
import com.antelope.ci.bus.server.service.UserStoreServerService;
import com.antelope.ci.bus.server.service.auth.AuthService;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月10日		下午1:05:08 
 */
public abstract class BusServerTemplateActivator extends BusActivator {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.BusActivator#customInit()
	 */
	@Override
	protected void customInit() throws CIBusException {
		beforeCustomInit();
		serviceList.add(UserStoreServerService.NAME);
		serviceList.add(AuthService.NAME);
		afterCustomInit();
	}
	
	public static List<AuthService> getAuthServices() {
		List<AuthService> authServices = new ArrayList<AuthService>();
		List serviceList = getServices(AuthService.NAME);
		if (serviceList != null) {
			for (Object service : serviceList)
				authServices.add((AuthService) service);
		}
		return authServices;
	}
	
	public static UserStoreServerService getUserStoreService() {
		return (UserStoreServerService) getUsingService(UserStoreServerService.NAME);
	}
	
	protected abstract void beforeCustomInit() throws CIBusException;
	
	protected abstract void afterCustomInit() throws CIBusException;
}

