// com.antelope.ci.bus.portal.configuration.xo.EU_Display.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.meta;

import java.io.Serializable;

import com.antelope.ci.bus.common.StringUtil;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-12		下午12:29:26 
 */
public enum EU_Display implements Serializable {
	SINGLE("single"),
	JOINT("joint");
	
	private String name;
	private EU_Display(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static EU_Display toDisplay(String name) {
		if (StringUtil.empty(name))
			return SINGLE;
		for (EU_Display d : EU_Display.values())
			if (d.getName().equalsIgnoreCase(name))
				return d;
		
		return SINGLE;
	}
}

