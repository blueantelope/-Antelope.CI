// com.antelope.ci.bus.portal.configuration.xo.RenderDelimiter.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.Serializable;

import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Position;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.Margin;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-11		下午5:29:49 
 */
@XmlEntity(name="delimiter")
public class RenderDelimiter implements Serializable {
	private String value;
	private String position;
	private String margin;
	
	@XmlAttribute(name="value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@XmlAttribute(name="position")
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	@XmlAttribute(name="margin")
	public String getMargin() {
		return margin;
	}
	public void setMargin(String margin) {
		this.margin = margin;
	}
	
	public Margin getMarginObject() {
		return Margin.parse(margin);
	}
	
	public EU_Position getEU_Position() {
		return EU_Position.toPosition(position);
	}
}

