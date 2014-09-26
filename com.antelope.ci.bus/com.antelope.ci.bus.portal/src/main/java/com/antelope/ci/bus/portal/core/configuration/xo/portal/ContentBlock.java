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
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.XOUtil;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.CommonValue;
import com.antelope.ci.bus.portal.core.shell.PortalShellText;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-9-2		下午3:07:09 
 */
@XmlEntity(name="block")
public class ContentBlock implements Serializable {
	private String action;
	private String active;
	private CommonValue cvalue;
	
	@XmlAttribute(name="action")
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public boolean action() {
		return XOUtil.on_off(action);
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
	
	@XmlElement(name="value")
	public CommonValue getCvalue() {
		return cvalue;
	}
	public void setCvalue(CommonValue cvalue) {
		this.cvalue = cvalue;
	}
	
	public String getValue() {
		if (null != cvalue)
			return cvalue.getValue();
		return null;
	}
	
	public String getShellValue() {
		String shellValue = null;
		if (null != cvalue) {
			shellValue = cvalue.getShellValue();
			if (available()) {
				if (cvalue.focus())
					shellValue = PortalShellText.genFocusText(shellValue);
				shellValue = PortalShellText.genBlockText(shellValue);
			}
		}
		return shellValue;
	}
	
	public void defaultSet() {
		this.action = "off";
		this.active = "off";
	}
	
	public boolean available() {
		if (action() && active())
			return true;
		return false;
	}
}