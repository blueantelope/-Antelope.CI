// com.antelope.ci.bus.gate.service.user.UserGateService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api.user;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.engine.manager.user.EngineUserManager;
import com.antelope.ci.bus.engine.manager.user.EngineUserManagerConstant;
import com.antelope.ci.bus.gate.api.GateApi;
import com.antelope.ci.bus.gate.api.IGateApi;
import com.antelope.ci.bus.gate.api.message.GateInMessage;
import com.antelope.ci.bus.gate.api.message.GateOutMessage;
import com.antelope.ci.bus.osgi.Inject;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月23日		下午3:54:39 
 */
@GateApi(oc=0x01)
public class UserGateApi implements IGateApi {
	private final static Logger log = Logger.getLogger(UserGateApi.class);
	private EngineUserManager userManager;
	
	@Inject(name=EngineUserManagerConstant.SERVICE_NAME)
	public void setUserManager(EngineUserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public GateOutMessage ls(GateInMessage in) {
		log.info(actionInfo("ls"));
		
		return null;
	}

	@Override
	public GateOutMessage add(GateInMessage in) {
		log.info(actionInfo("add"));
		
		return null;
	}

	@Override
	public GateOutMessage delete(GateInMessage in) {
		log.info(actionInfo("delete"));
		
		return null;
	}

	@Override
	public GateOutMessage mod(GateInMessage in) {
		log.info(actionInfo("edit"));
		
		return null;
	}
	
	protected String actionInfo(String actionName) {
		return "User Gate Api -> " + actionName;
	}
}
