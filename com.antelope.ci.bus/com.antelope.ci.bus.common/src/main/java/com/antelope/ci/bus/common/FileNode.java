// com.antelope.ci.bus.common.FileNode.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-21		下午10:45:21 
 */
public class FileNode {
	protected String path;
	protected FILE_TYPE type;
	protected List<FileNode> childNodes;
	
	public FileNode() {
		childNodes = new ArrayList<FileNode>();
	}
	
	public FileNode(String path, FILE_TYPE type) {
		this();
		this.path = path;
		this.type = type;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public FILE_TYPE getType() {
		return type;
	}
	public void setType(FILE_TYPE type) {
		this.type = type;
	}
	public List<FileNode> getChildNodes() {
		return childNodes;
	}
	public void setChildNodes(List<FileNode> childNodes) {
		this.childNodes = childNodes;
	}
	public void addChildNode(FileNode childNode) {
		childNodes.add(childNode);
	}

	public List<FileNode> getChildFileList() {
		List<FileNode> nodeList = new ArrayList<FileNode>();
		getChildFile(nodeList, this);
		return nodeList;
	}
	private void getChildFile(List<FileNode> nodeList , FileNode rootNode) {
		if (rootNode.type == FILE_TYPE.DIRECTOTRY) {
			File root = new File(path);
			List<FileNode> dirList = new ArrayList<FileNode>();
			for (File child : root.listFiles()) {
				FileNode childNode;
				if (child.isDirectory()) {
					childNode = new FileNode(rootNode.path, FILE_TYPE.DIRECTOTRY);
					getChildFile(nodeList, childNode);
				} else {
					childNode = new FileNode(rootNode.path, FILE_TYPE.FILE);
				}
				nodeList.add(childNode); 
			}
		}
	}
}

