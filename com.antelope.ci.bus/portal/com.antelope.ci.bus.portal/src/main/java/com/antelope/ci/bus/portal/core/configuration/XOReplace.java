// com.antelope.ci.bus.portal.core.configuration.XoReplace.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration;



/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-28		下午5:41:02 
 */
public class XOReplace {
	protected Object parent;
	protected String setter;
	protected Object value;
	
	public XOReplace(Object parent, String setter, Object value) {
		super();
		this.parent = parent;
		this.setter = setter;
		this.value = value;
	}

	// getter and setter
	public Object getParent() {
		return parent;
	}
	public void setParent(Object parent) {
		this.parent = parent;
	}

	public String getSetter() {
		return setter;
	}
	public void setSetter(String setter) {
		this.setter = setter;
	}

	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public <T extends XOReplace> boolean exist(T comPr) {
		if (parent == comPr.getParent() && setter == comPr.getSetter()
				&& value == comPr.getValue())
			return true;
		return false;
	}
}

