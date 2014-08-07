// com.antelope.ci.bus.portal.core.configuration.xo.form.Input.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.form;

import java.io.Serializable;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Widget;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-6		下午6:14:07 
 */
@XmlEntity(name="input")
public class Input implements Serializable {
	private String type;
	private String name;
	private String widget;
	private Box box;
	
	@XmlAttribute(name="type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute(name="widget")
	public String getWidget() {
		return widget;
	}
	public void setWidget(String widget) {
		this.widget = widget;
	}
	public EU_Widget toWidget() throws CIBusException {
		if (!StringUtil.empty(widget))
			return EU_Widget.fromName(widget.trim());
		throw new CIBusException("widget empty");
	}
	
	@XmlElement(name="box")
	public Box getBox() {
		return box;
	}
	public void setBox(Box box) {
		this.box = box;
	}
}

