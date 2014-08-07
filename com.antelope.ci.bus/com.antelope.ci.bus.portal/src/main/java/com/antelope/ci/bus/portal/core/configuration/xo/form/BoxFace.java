// com.antelope.ci.bus.portal.core.configuration.xo.form.BoxFace.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.form;

import java.io.Serializable;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlText;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-6		下午6:22:57 
 */
public class BoxFace implements Serializable {
	private static final String PREFIX = "{";
	private static final String SUFFIX = "}";
	protected String active;
	protected String value;
	
	@XmlAttribute(name="active")
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	
	@XmlText
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = StringUtil.formatString(value, PREFIX, SUFFIX);
	}
	
	public String getExpression() {
		if (!StringUtil.empty(active) && "on".equalsIgnoreCase(active.trim()))
				return value;
		return null;
	}
}

