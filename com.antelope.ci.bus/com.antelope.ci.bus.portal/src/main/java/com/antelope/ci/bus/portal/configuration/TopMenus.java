// com.antelope.ci.bus.common.xml.test.TopMenus.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration;

import java.util.List;

import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-15		下午5:55:21 
 */
@XmlEntity(name="top-menus")
public class TopMenus {
	private Integer extends_start;
	private List<TopMenu> menuList;
	
	@XmlAttribute(name="extends_start")
	public Integer getExtends_start() {
		return extends_start;
	}
	public void setExtends_start(Integer extends_start) {
		this.extends_start = extends_start;
	}
	
	@XmlElement(name="top-menu", isList=true, listClass=TopMenu.class)
	public List<TopMenu> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<TopMenu> menuList) {
		this.menuList = menuList;
	}
}

