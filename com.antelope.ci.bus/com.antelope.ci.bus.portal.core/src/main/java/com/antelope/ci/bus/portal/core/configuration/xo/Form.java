// com.antelope.ci.bus.portal.core.configuration.xo.Form.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo;

import java.io.Serializable;

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Content;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Style;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-11		上午11:52:50 
 */
@XmlEntity(name="form")
public class Form implements Serializable {
	private Style style;
	private Content content;

	@XmlElement(name="style")
	public Style getStyle() {
		return style;
	}
	public void setStyle(Style style) {
		this.style = style;
	}
	
	@XmlElement(name="content")
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
}

