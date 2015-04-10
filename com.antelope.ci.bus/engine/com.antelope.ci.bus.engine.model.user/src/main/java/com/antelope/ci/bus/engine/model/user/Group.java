// com.antelope.ci.bus.engine.model.user.Group.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model.user;

import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月7日		下午1:56:07 
 */
public class Group {
	protected int id;
	protected String name;
	protected Set<User> userSet;
	
	public Group() {
		super();
	}
	
	public Group(int id) {
		super();
		this.id = id;
	}
	
	public Group(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	// getter and setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<User> getUserSet() {
		return userSet;
	}
	public void setUserSet(Set<User> userSet) {
		this.userSet = userSet;
	}
	
	public static Set<Group> initGroupSet() {
		Set<Group> groupSet = new HashSet<Group>();
		groupSet.add(new Group(0, "admin"));
		return groupSet;
	}
}
