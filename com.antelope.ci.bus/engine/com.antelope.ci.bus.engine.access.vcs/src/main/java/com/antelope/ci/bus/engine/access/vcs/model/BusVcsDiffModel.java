// com.antelope.ci.bus.vcs.model.BusVcsDiffModel.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.model;

import java.util.List;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-20		下午6:28:36 
 */
public class BusVcsDiffModel extends BusVcsModel {
	protected List<String> pathList;
	protected boolean cached;
	protected String oldTree;
	protected String newTree;
	protected String srcPrefix;
	protected String destPrefix;
	
	
	public List<String> getPathList() {
		return pathList;
	}
	public void setPathList(List<String> pathList) {
		this.pathList = pathList;
	}
	public boolean isCached() {
		return cached;
	}
	public void setCached(boolean cached) {
		this.cached = cached;
	}
	public String getOldTree() {
		return oldTree;
	}
	public void setOldTree(String oldTree) {
		this.oldTree = oldTree;
	}
	public String getNewTree() {
		return newTree;
	}
	public void setNewTree(String newTree) {
		this.newTree = newTree;
	}
	public String getSrcPrefix() {
		return srcPrefix;
	}
	public void setSrcPrefix(String srcPrefix) {
		this.srcPrefix = srcPrefix;
	}
	public String getDestPrefix() {
		return destPrefix;
	}
	public void setDestPrefix(String destPrefix) {
		this.destPrefix = destPrefix;
	}
}

