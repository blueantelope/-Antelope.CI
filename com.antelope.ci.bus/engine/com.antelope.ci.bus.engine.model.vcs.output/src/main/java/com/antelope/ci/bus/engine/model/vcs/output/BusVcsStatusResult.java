// com.antelope.ci.bus.vcs.result.BusVcsStatusResult.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model.vcs.output;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-20		下午7:04:52 
 */
public class BusVcsStatusResult extends BusVcsResult {
	protected List<String> addList;
	protected List<String> deleteList;
	protected List<String> changeList;
	
	public BusVcsStatusResult() {
		addList = new ArrayList<String>();
		deleteList = new ArrayList<String>();
		changeList = new ArrayList<String>();
	}
	
	public List<String> getAddList() {
		return addList;
	}
	public void setAddList(List<String> addList) {
		this.addList = addList;
	}
	public void addAdd(String add) {
		addList.add(add);
	}
	
	public List<String> getDeleteList() {
		return deleteList;
	}
	public void setDeleteList(List<String> deleteList) {
		this.deleteList = deleteList;
	}
	public void addDelete(String delete) {
		deleteList.add(delete);
	}
	
	public List<String> getChangeList() {
		return changeList;
	}
	public void setChangeList(List<String> changeList) {
		this.changeList = changeList;
	}
	public void addChange(String change) {
		changeList.add(change);
	}
}

