// com.antelope.ci.bus.vcs.result.BusVcsListResult.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model.vcs.output;

import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.FileNode;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-20		下午6:25:33 
 */
public class BusVcsListResult extends BusVcsResult {
	protected List<FileNode> nodeList;
	
	public BusVcsListResult() {
		nodeList = new ArrayList<FileNode>();
	}

	public List<FileNode> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<FileNode> nodeList) {
		this.nodeList = nodeList;
	}
	
	public void addNode(FileNode node) {
		nodeList.add(node);
	}
}

