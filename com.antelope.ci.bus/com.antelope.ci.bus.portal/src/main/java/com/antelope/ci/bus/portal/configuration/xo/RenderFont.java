// com.antelope.ci.bus.portal.configuration.xo.RenderFont.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import java.io.Serializable;

import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-31		下午5:10:53 
 */
@XmlEntity(name="font")
public class RenderFont implements Serializable {
	private String style;
	private String size;
	private String mark;

	@XmlAttribute(name="style")
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	@XmlAttribute(name="size")
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@XmlAttribute(name="mark")
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public EU_FontMark toEU_Mark() {
		return EU_FontMark.toMark(mark);
	}
	
	public EU_FontStyle toEU_Style() {
		return EU_FontStyle.toStyle(style);
	}
	
	public EU_FontSize toEU_Size() {
		return EU_FontSize.toSize(size);
	}
	
	public ContentFont toContentFont() {
		ContentFont cf = new ContentFont();
		cf.setMark(toEU_Mark());
		cf.setSize(toEU_Size());
		cf.setSytle(toEU_Style());
		return cf;
	}
}

