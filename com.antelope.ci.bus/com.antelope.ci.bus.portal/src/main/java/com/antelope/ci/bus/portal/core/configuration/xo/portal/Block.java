// com.antelope.ci.bus.portal.core.configuration.xo.portal.Block.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.Serializable;

import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlCdata;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.XOUtil;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.FontExpression;
import com.antelope.ci.bus.server.shell.ShellText;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-9-2		下午3:07:09 
 */
@XmlEntity(name="block")
public class Block extends CommonContent {
	private String focus;
	private String active;
	
	@XmlAttribute(name="focus")
	public String getFocus() {
		return focus;
	}
	public void setFocus(String focus) {
		this.focus = focus;
	}
	public boolean focus() {
		return XOUtil.on_off(focus);
	}
	
	@XmlAttribute(name="active")
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public boolean active() {
		return XOUtil.on_off(active);
	}
}