// com.antelope.ci.bus.portal.core.configuration.xo.form.Value.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.form;

import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.server.shell.ShellText;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-11		下午2:52:39 
 */
@XmlEntity(name="field")
public class Field extends Widget {
	private String value;

	@XmlAttribute(name="value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public ShellText toShellText() {
		return super.toShellText(value);
	}
}