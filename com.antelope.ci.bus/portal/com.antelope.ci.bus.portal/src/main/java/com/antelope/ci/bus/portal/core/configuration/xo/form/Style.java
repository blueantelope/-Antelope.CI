// com.antelope.ci.bus.portal.core.configuration.xo.Style.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.form;

import java.io.Serializable;

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.FontExpression;
import com.antelope.ci.bus.server.shell.base.ShellText;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-11		下午2:35:59 
 */
@XmlEntity(name="style")
public class Style implements Serializable {
	private StyleAlign align;
	private StyleFont font;
	
	@XmlElement(name="align")
	public StyleAlign getAlign() {
		return align;
	}
	public void setAlign(StyleAlign align) {
		this.align = align;
	}
	
	@XmlElement(name="font")
	public StyleFont getFont() {
		return font;
	}
	public void setFont(StyleFont font) {
		this.font = font;
	}
	
	public static ShellText genShellText(Style style, String value) {
		ShellText text = new ShellText();
		text.setText(value);
		StyleFont sfont = style.getFont();
		FontExpression fontExp = sfont.toFontExpression();
		text.setFont_mark(fontExp.getMark().getCode());
		text.setFont_size(fontExp.getSize().getCode());
		text.setFont_style(fontExp.getSytle().getCode());
		
		return text;
	}
}

