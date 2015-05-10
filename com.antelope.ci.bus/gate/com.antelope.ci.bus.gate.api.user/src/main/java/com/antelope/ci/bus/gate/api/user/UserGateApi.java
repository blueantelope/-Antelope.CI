// com.antelope.ci.bus.gate.service.user.UserGateService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api.user;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.manager.user.EngineUserManager;
import com.antelope.ci.bus.engine.manager.user.EngineUserManagerConstant;
import com.antelope.ci.bus.engine.model.ModelUtil;
import com.antelope.ci.bus.engine.model.user.User;
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
	
	private User in2User(GateInMessage in) throws CIBusException {
		User user = new User();
		user.fromMessage(in);
		return user;
	}
	
	private Map<String, String> makeCondition(User user) throws CIBusException {
		List<Map<String, String>> bodyList = user.parseBody();
		if (bodyList == null || bodyList.isEmpty())
			return null;
		return ModelUtil.makeCoditionMap(bodyList);
	}
	
	@Inject(name=EngineUserManagerConstant.SERVICE_NAME)
	public void setUserManager(EngineUserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public GateOutMessage ls(GateInMessage in) throws CIBusException {
		log.info(actionInfo("ls"));
		User inUser = in2User(in);
		Map<String, String> ucondition = makeCondition(inUser);
		List<User> userList = userManager.lsUser(inUser);
		
		return null;
	}

	@Override
	public GateOutMessage add(GateInMessage in) throws CIBusException {
		log.info(actionInfo("add"));
		
		return null;
	}

	@Override
	public GateOutMessage rm(GateInMessage in) throws CIBusException {
		log.info(actionInfo("rm"));
		
		return null;
	}

	@Override
	public GateOutMessage mod(GateInMessage in) throws CIBusException {
		log.info(actionInfo("edit"));
		
		return null;
	}
	
	protected String actionInfo(String actionName) throws CIBusException {
		return "User Gate Api -> " + actionName;
	}
}
