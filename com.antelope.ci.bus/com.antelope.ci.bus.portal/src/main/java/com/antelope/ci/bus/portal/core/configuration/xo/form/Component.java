// com.antelope.ci.bus.portal.core.configuration.xo.form.Textfield.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.form;

import java.io.Serializable;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_ComponentType;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-11		下午2:50:55 
 */
@XmlEntity(name="component")
public class Component implements Serializable {
	private Label label;
	private Field field;
	private String type;
	private String name;
	
	@XmlElement(name="label")
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	
	@XmlElement(name="field")
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	
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
	
	public EU_ComponentType toComponentType() throws CIBusException {
		return EU_ComponentType.fromName(type);
	}
}