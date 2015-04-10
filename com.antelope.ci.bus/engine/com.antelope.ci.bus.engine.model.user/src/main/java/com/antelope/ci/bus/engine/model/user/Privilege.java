// com.antelope.ci.bus.engine.model.user.Privilege.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model.user;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月7日		下午3:02:51 
 */
public class Privilege {
	protected Group group;
	protected User user;
	protected int group_privilege;
	protected int user_privilege;
	protected int others_privilege;
	
	public Privilege() {
		super();
	}
	
	public Privilege(Group group, User user, int group_privilege,
			int user_privilege, int others_privilege) {
		super();
		this.group = group;
		this.user = user;
		this.group_privilege = group_privilege;
		this.user_privilege = user_privilege;
		this.others_privilege = others_privilege;
	}

	// getter and setter
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public int getGroup_privilege() {
		return group_privilege;
	}
	public void setGroup_privilege(int group_privilege) {
		this.group_privilege = group_privilege;
	}
	
	public int getUser_privilege() {
		return user_privilege;
	}
	public void setUser_privilege(int user_privilege) {
		this.user_privilege = user_privilege;
	}
	
	public int getOthers_privilege() {
		return others_privilege;
	}
	public void setOthers_privilege(int others_privilege) {
		this.others_privilege = others_privilege;
	}
}
