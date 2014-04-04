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
	SMALL(1, "small"),
	MEDIUM(2, "medium"),
	LARGE(3, "large");
	
	private int code;
	private String name;
	private EU_FontSize(int code, String name) {
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
	
	public static EU_FontSize toSize(String name) {
		if (StringUtil.empty(name))
			return MEDIUM;
		for (EU_FontSize fs : EU_FontSize.values())
			if (fs.getName().equalsIgnoreCase(name.trim()))
				return fs;
		
		return MEDIUM;
	}
	
	public static EU_FontSize toSize(int code) {
		for (EU_FontSize fs : EU_FontSize.values())
			if (fs.getCode() == code)
				return fs;
		
		return MEDIUM;
	}
}

