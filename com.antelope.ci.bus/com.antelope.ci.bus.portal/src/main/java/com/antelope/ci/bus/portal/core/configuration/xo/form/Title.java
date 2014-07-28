// com.antelope.ci.bus.portal.core.configuration.xo.form.Title.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.form;

import java.io.Serializable;

import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.FontExpression;
import com.antelope.ci.bus.server.shell.ShellText;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-28		上午9:35:38 
 */
@XmlEntity(name="title")
public class Title implements Serializable {
	private String value;
	private Style style;
	
	@XmlAttribute(name="value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@XmlElement(name="style")
	public Style getStyle() {
		return style;
	}
	public void setStyle(Style style) {
		this.style = style;
	}
	
	@Override public String toString() {
		StyleFont font = style.getFont();
		if (font != null)
			return toShellText().toString();
		return value;
	}
	
	public ShellText toShellText() {
		ShellText text = new ShellText();
		text.setText(value);
		StyleFont font = style.getFont();
		FontExpression fontExp = font.toFontExpression();
		text.setFont_mark(fontExp.getMark().getCode());
		text.setFont_size(fontExp.getSize().getCode());
		text.setFont_style(fontExp.getSytle().getCode());
		
		return text;
	}
	
	public boolean isShellText() {
		return ShellText.isShellText(value);
	}
}

