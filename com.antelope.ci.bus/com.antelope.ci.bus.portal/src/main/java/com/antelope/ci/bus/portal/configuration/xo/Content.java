// com.antelope.ci.bus.portal.configuration.xo.Content.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import java.io.Serializable;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.xml.XmlCdata;
import com.antelope.ci.bus.common.xml.XmlEntity;
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
	private ContentFont font;

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

	public ContentFont getFont() {
		return font;
	}

	public void setFont(ContentFont font) {
		this.font = font;
	}
	
	public boolean isShellText() {
		if (StringUtil.empty(value))
			return false;
		String str = value.trim();
		if (StringUtil.startsWithIgnoreCase(str, "<font"))
			return true;
		return false;
	}
	
	public String getShellValue() {
		if (isShellText()) {
			ShellText text = ShellText.toShellText(value.trim());
			return text.getText();
		}
		
		return value;
	}
}

