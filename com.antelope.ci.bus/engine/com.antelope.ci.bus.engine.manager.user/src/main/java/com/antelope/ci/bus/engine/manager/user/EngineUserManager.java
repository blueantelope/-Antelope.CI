// com.antelope.ci.bus.engine.manager.user.EngineUserManager.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager.user;

import java.util.List;

import com.antelope.ci.bus.engine.model.user.Group;
import com.antelope.ci.bus.engine.model.user.User;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月30日		下午2:30:51 
 */
public interface EngineUserManager {
	public List<Group> lsGroup(Group group);
	
	public void addGroup(Group group);
	
	public void rmGroup(Group group);
	
	public void alterGroup(Group group, Group update);
	
	public List<User> lsUser(User user);
	
	public void addUser(User user);
	
	public void rmUser(User user);
	
	public void alterUser(User user, User update);
}
