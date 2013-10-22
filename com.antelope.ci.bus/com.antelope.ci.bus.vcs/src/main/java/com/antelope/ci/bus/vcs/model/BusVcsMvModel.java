// com.antelope.ci.bus.vcs.model.BusVcsMvModel.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.vcs.model;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-20		下午6:24:20 
 */
public class BusVcsMvModel extends BusVcsModel {
	protected boolean rebase;
	protected String branch;
	protected String oldname;
	protected String newname;
	
	// getter and setter
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getOldname() {
		return oldname;
	}
	public void setOldname(String oldname) {
		this.oldname = oldname;
	}
	public String getNewname() {
		return newname;
	}
	public void setNewname(String newname) {
		this.newname = newname;
	}
	public boolean isRebase() {
		return rebase;
	}
	public void setRebase(boolean rebase) {
		this.rebase = rebase;
	}
}

