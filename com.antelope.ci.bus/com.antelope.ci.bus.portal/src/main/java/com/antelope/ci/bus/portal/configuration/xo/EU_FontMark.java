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
public enum EU_FontMark implements Serializable {
	NORMAL(1, "normal"),
	LT(2, "lt"),
	SHADE(3, "shade");
	
	private int code;
	private String name;
	private EU_FontMark(int code, String name) {
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
	
	public static EU_FontMark toMark(String name) {
		if (StringUtil.empty(name))
			return NORMAL;
		for (EU_FontMark s : EU_FontMark.values())
			if (s.getName().equalsIgnoreCase(name))
				return s;
		
		return NORMAL;
	}
	
	public static EU_FontMark toMark(int code) {
		for (EU_FontMark s : EU_FontMark.values())
			if (s.getCode() == code)
				return s;
		
		return NORMAL;
	}
}

