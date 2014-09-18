// com.antelope.ci.bus.portal.core.configuration.xo.meta.Value.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.meta;

import java.io.Serializable;

import com.antelope.ci.bus.common.ResourceUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.xml.XmlCdata;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.RenderFont;
import com.antelope.ci.bus.server.shell.ShellText;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-9-10		上午11:54:38 
 */
@XmlEntity(name="value")
public class CommonValue implements Serializable {
	protected String value;
	protected FontExpression font;

	public CommonValue() {
		super();
	}
	
	public CommonValue(String value) {
		super();
		this.value = value;
	}
	
	@XmlCdata public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void replaceValue(BasicConfigrationReader[] readerList) {
		for (BasicConfigrationReader reader : readerList)
			if (ResourceUtil.needReplace(value))
				value = ResourceUtil.replaceLableForReader(value, reader);
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
	
	public static String toShellText(String value, RenderFont font) {
		CommonValue commonValue = new CommonValue(value);
		if (ShellText.isShellText(value)) {
			return commonValue.getShellValue();
		} else {
			commonValue.setFont(font.toFontExpression());
			return commonValue.toShellText().toString();
		}
	}
	
	public void addAfterValue(String s) {
		if (StringUtil.empty(value))
			value = s;
		else
			value += s;
	}
	
	public void addForeValue(String s) {
		if (StringUtil.empty(value))
			value = s;
		else
			value = s + value;
	}
	
	public CommonValue clone() {
		CommonValue cloneCommonValue = new CommonValue();
		cloneCommonValue.setFont(font);
		cloneCommonValue.setValue(value);
		return cloneCommonValue;
	}
}

