// com.antelope.ci.bus.vcs.model.BusVcsDeleteBranchModel.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.model;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-27		下午9:40:21 
 */
public class BusVcsDeleteBranchModel extends BusVcsModel {
	protected List<String> nameList;
	
	public BusVcsDeleteBranchModel() {
		nameList = new ArrayList<String>();
	}

	public List<String> getNameList() {
		return nameList;
	}

	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}

	public void addName(String name) {
		nameList.add(name);
	}
}

