// com.antelope.ci.bus.vcs.result.BusVcsRmModel.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.model;

import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.FILE_TYPE;
import com.antelope.ci.bus.common.FileNode;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-20		下午6:23:50 
 */
public class BusVcsRmModel extends BusVcsModel {
	protected boolean cached;
	protected List<FileNode> rmList;

	public BusVcsRmModel() {
		rmList = new ArrayList<FileNode>();
	}
	
	public void addDirectory(String subPath) {
		rmList.add(new FileNode(subPath, FILE_TYPE.DIRECTOTRY));
	}
	
	public void addFile(String subPath) {
		rmList.add(new FileNode(subPath, FILE_TYPE.FILE));
	}

	public List<FileNode> getRmList() {
		return rmList;
	}

	public void setRmList(List<FileNode> rmList) {
		this.rmList = rmList;
	}

	public boolean isCached() {
		return cached;
	}

	public void setCached(boolean cached) {
		this.cached = cached;
	}
}

