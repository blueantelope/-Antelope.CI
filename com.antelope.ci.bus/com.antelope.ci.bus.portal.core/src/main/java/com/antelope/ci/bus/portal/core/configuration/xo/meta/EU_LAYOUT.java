// com.antelope.ci.bus.portal.configuration.LAYOUT_PLACE.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.meta;

import java.io.Serializable;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 布局
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-17		下午12:39:06 
 */
public enum EU_LAYOUT implements Serializable {
	NORTH("north"),
	SOUTH("south"),
	CENTER("center"),
	WEST("west"),
	EAST("east");
	
	private String name;
	private EU_LAYOUT(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public @Override String toString() {
		return name;
	}
	
	public static EU_LAYOUT toLayout(String name) throws CIBusException {
		for (EU_LAYOUT layout : EU_LAYOUT.values()) {
			if (layout.getName().equalsIgnoreCase(name)) 
				return layout;
		}
		
		throw new CIBusException("unknow layout");
	}
}

