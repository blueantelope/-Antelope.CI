// com.antelope.ci.bus.engine.manager.user.realization.EngineUserManagerRealization.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager.user.realization;

import java.util.List;
import java.util.Map;

import com.antelope.ci.bus.common.aql.AQLKeyword;
import com.antelope.ci.bus.engine.manager.EngineManager;
import com.antelope.ci.bus.engine.manager.EngineService;
import com.antelope.ci.bus.engine.manager.user.CommonEngineUserManager;
import com.antelope.ci.bus.engine.manager.user.EngineUserManagerConstant;
import com.antelope.ci.bus.engine.model.user.Group;
import com.antelope.ci.bus.engine.model.user.User;

/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月30日		下午2:38:22 
 */
@EngineService
@EngineManager(service_name=EngineUserManagerConstant.SERVICE_NAME)
public class EngineUserManagerRealization extends CommonEngineUserManager {

	@Override
	public List<Group> lsGroup(Group group) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public void addGroup(Group group) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rmGroup(Group group) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void alterGroup(Group group, Group update) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> lsUser(User user) {
		Map<String, AQLKeyword> keywordMap = user.genKeywordMap();
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public void addUser(User user) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rmUser(User user) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void alterUser(User user, User update) {
		
		// TODO Auto-generated method stub
		
	}


}
