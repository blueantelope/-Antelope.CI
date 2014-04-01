// com.antelope.ci.bus.portal.configuration.xo.EU_Style.java
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
 * @Date	 2014-3-31		下午5:12:04 
 */
public enum EU_FontStyle implements Serializable {
	NORMAL("normal"),
	SHADE("shade"),
	LT("lt");
	
	private String name;
	private EU_FontStyle(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	public static EU_FontStyle toStyle(String name) {
		if (StringUtil.empty(name))
			return NORMAL;
		for (EU_FontStyle s : EU_FontStyle.values())
			if (s.getName().equalsIgnoreCase(name))
				return s;
		
		return NORMAL;
	}
}

