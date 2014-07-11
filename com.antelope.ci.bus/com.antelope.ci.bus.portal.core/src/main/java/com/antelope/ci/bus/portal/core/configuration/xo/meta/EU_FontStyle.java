// com.antelope.ci.bus.portal.configuration.xo.EU_FontStyle.java
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
 * @Date	 2014-4-2		下午6:27:43 
 */
public enum EU_FontStyle implements Serializable {
	NORMAL(1, "normal"),
	BOLD(2, "bold"),
	ITALIC(3, "italic");
	
	private int code;
	private String name;
	private EU_FontStyle(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return code;
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
	
	public static EU_FontStyle toStyle(int code) {
		for (EU_FontStyle s : EU_FontStyle.values())
			if (s.getCode() == code)
				return s;
		
		return NORMAL;
	}
}


