// com.antelope.ci.bus.vcs.model.BusVcsVersionResult.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.model;

import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.engine.access.vcs.result.BusVcsResult;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-23		下午10:50:35 
 */
public class BusVcsVersionResult extends BusVcsResult {
	protected List<String> versionList;
	
	public BusVcsVersionResult() {
		versionList = new ArrayList<String>();
	}

	public List<String> getVersionList() {
		return versionList;
	}

	public void setVersionList(List<String> versionList) {
		this.versionList = versionList;
	}

	public void addVersion(String version) {
		versionList.add(version);
	}
}

