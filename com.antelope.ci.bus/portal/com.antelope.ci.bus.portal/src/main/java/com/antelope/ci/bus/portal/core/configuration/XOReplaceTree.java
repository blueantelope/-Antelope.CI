// com.antelope.ci.bus.portal.core.configuration.XOReplaceTree.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-28		下午5:49:21 
 */
public class XOReplaceTree<R extends XOReplace, T extends XOReplaceTree> {
	protected boolean isRoot;
	protected R value;
	protected List<T> children;
	protected boolean isList;
	protected boolean stringValue;
	
	public XOReplaceTree() {
		super();
		children = new ArrayList<T>();
		isRoot = false;
		isList = false;
		stringValue = false;
	}
	
	public void isRoot() {
		this.isRoot = true;
	}
	public void isList() {
		this.isList = true;
	}
	public void isStringValue() {
		this.stringValue = true;
	}
	public boolean hasValue() {
		return !isRoot && !isList && stringValue;
	}
	public R getValue() {
		return value;
	}
	public void setValue(R value) {
		this.value = value;
	}
	
	public List<T> getChildren() {
		return children;
	}
	
	public void setChildren(List<T> children) {
		this.children = children;
	}
	
	public void addChild(T child) {
		children.add(child);
	}
}

