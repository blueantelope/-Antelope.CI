// com.antelope.ci.bus.common.xml.test.PortalTerminal.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test.xml;

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-15		下午5:54:11 
 */
@XmlEntity(name="portal-terminal")
public class PortalTerminal {
	private Banner banner;
	private TopMenus topMenus;
	
	@XmlElement(name="banner")
	public Banner getBanner() {
		return banner;
	}
	
	public void setBanner(Banner banner) {
		this.banner = banner;
	}
	
	@XmlElement(name="top-menus")
	public TopMenus getTopMenus() {
		return topMenus;
	}
	
	public void setTopMenus(TopMenus topMenus) {
		this.topMenus = topMenus;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("banner=");
		if (banner != null)
			sb.append(banner.getText());
		sb.append(",topMenus={");
		if (topMenus != null) {
			sb.append("extends_start=" + topMenus.getExtends_start() + ",");
			sb.append("{");
			if (topMenus.getMenuList() != null) {
				for (TopMenu topMenu : topMenus.getMenuList()) {
					sb.append("topMenu={");
					sb.append("name=" + topMenu.getName() + ",");
					sb.append("sort=" + topMenu.getSort() + ",");
					sb.append("}");
				}
			}
			sb.append("}");
		}
		sb.append("}");
		
		return sb.toString();
	}
}

