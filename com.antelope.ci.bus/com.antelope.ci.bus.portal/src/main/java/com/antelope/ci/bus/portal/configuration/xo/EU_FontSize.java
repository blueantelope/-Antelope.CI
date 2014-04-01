// com.antelope.ci.bus.portal.configuration.xo.EU_FontSize.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import java.io.Serializable;

import com.antelope.ci.bus.common.StringUtil;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-31		下午5:17:36 
 */
public enum EU_FontSize implements Serializable {
	SMALL("small"),
	MEDIUM("medium"),
	LARGE("large");
	
	private String name;
	private EU_FontSize(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	public static EU_FontSize toSize(String name) {
		if (StringUtil.empty(name))
			return MEDIUM;
		for (EU_FontSize fs : EU_FontSize.values())
			if (fs.getName().equalsIgnoreCase(name.trim()))
				return fs;
		
		return MEDIUM;
	}
}

