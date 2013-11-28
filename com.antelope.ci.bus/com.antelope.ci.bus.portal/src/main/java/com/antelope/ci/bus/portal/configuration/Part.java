// com.antelope.ci.bus.portal.configuration.Part.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration;

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-19		下午9:15:09 
 */
@XmlEntity(name="part")
public class Part {
	private TopMenu topMenu;

	@XmlElement(name="top-menu")
	public TopMenu getTopMenu() {
		return topMenu;
	}

	public void setTopMenu(TopMenu topMenu) {
		this.topMenu = topMenu;
	}
}
