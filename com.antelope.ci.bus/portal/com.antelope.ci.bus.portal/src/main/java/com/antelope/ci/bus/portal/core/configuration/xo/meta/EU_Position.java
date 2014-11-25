// com.antelope.ci.bus.portal.configuration.xo.EU_Position.java
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
 * @Date	 2014-3-11		下午5:09:33 
 */
public enum EU_Position implements Serializable {
	LEFT("left"),
	RIGHT("right"),
	CENTER("center"),
	START("start"),
	MIDDLE("middle"),
	END("end");
	
	private String name;
	private EU_Position(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static EU_Position toPosition(String name) {
		if (StringUtil.empty(name))
			return EU_Position.MIDDLE;
		
		for (EU_Position p : EU_Position.values())
			if (p.getName().equalsIgnoreCase(name))
				return p;
		
		return EU_Position.MIDDLE;
	}
}

