// com.antelope.ci.bus.portal.configuration.xo.Content.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.Serializable;

import com.antelope.ci.bus.common.xml.XmlCdata;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.FontExpression;
import com.antelope.ci.bus.server.shell.ShellText;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		下午8:03:52 
 */
@XmlEntity(name="content")
public class Content implements Serializable {
	private String value;
	private FontExpression font;
	
	public Content(String value) {
		super();
		this.value = value;
	}

	public Content() {
		super();
	}
	
	@XmlCdata
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FontExpression getFont() {
		return font;
	}

	public void setFont(FontExpression font) {
		this.font = font;
	}
	
	@Override public String toString() {
		if (font != null)
			return toShellText().toString();
		return value;
	}
	
	public ShellText toShellText() {
		ShellText text = new ShellText();
		text.setText(value);
		text.setFont_mark(font.getMark().getCode());
		text.setFont_size(font.getSize().getCode());
		text.setFont_style(font.getSytle().getCode());
		
		return text;
	}
	
	public boolean isShellText() {
		return ShellText.isShellText(value);
	}
	
	public String getShellValue() {
		if (isShellText()) {
			ShellText text = ShellText.toShellText(value.trim());
			return text.getText();
		}
		
		return value;
	}
}

