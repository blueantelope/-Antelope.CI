// com.antelope.ci.bus.portal.configuration.xo.PlacePartRender.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.Serializable;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Align;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-11		下午5:08:24 
 */
@XmlEntity(name="render")
public class Render implements Serializable {
	private RenderDelimiter delimiter;
	private RenderFont font;
	private String align;
	
	@XmlElement(name="delimiter")
	public RenderDelimiter getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(RenderDelimiter delimiter) {
		this.delimiter = delimiter;
	}
	
	@XmlElement(name="font")
	public RenderFont getFont() {
		return font;
	}
	public void setFont(RenderFont font) {
		this.font = font;
	}
	@XmlAttribute(name="align")
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	
	public EU_Align getEU_Align() {
		if (align == null)
			return null;
		try {
			return EU_Align.toAlign(align);
		} catch (CIBusException e) {
			DevAssistant.assert_exception(e);
			return null;
		}
	}
}

