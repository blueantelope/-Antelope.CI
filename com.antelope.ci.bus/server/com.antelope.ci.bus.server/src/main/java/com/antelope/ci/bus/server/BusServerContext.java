// com.antelope.ci.bus.server.BusServerTemplateContext.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.osgi.BusContext;
import com.antelope.ci.bus.server.service.UserStoreServerService;
import com.antelope.ci.bus.server.service.auth.AuthService;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月15日		下午4:49:25 
 */
public class BusServerContext extends BusContext {
	static final String CONTEXT_CLAZZ = BusServerContext.class.getName();
	
	@Override
	protected void init() {
		super.init();
		loadServiceList.add(UserStoreServerService.NAME);
		loadServiceList.add(AuthService.NAME);
	}
	
	public List<AuthService> getAuthServices() {
		List<AuthService> authServices = new ArrayList<AuthService>();
		List serviceList = getServices(AuthService.NAME);
		if (serviceList != null) {
			for (Object service : serviceList)
				authServices.add((AuthService) service);
		}
		return authServices;
	}
	
	public UserStoreServerService getUserStoreService() {
		return (UserStoreServerService) getUsingService(UserStoreServerService.NAME);
	}
}

