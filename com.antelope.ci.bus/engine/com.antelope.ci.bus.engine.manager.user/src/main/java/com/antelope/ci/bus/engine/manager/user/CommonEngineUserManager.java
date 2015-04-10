// com.antelope.ci.bus.engine.manager.user.CommonEngineUserManager.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager.user;

import java.util.Set;

import com.antelope.ci.bus.engine.manager.CommonEngineManager;
import com.antelope.ci.bus.engine.model.user.Domain;
import com.antelope.ci.bus.engine.model.user.Group;
import com.antelope.ci.bus.engine.model.user.User;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月30日		下午2:31:13 
 */
public abstract class CommonEngineUserManager
		extends CommonEngineManager implements EngineUserManager { 
	protected Set<Domain> domainSet;
	protected Set<Group> groupSet;
	protected Set<User> userSet;
	
	public CommonEngineUserManager() {
		super();
		init();
	}
	
	protected void init() {
		domainSet = Domain.initDomainSet();
		groupSet = Group.initGroupSet();
		userSet = User.initUserSet();
	}
}
