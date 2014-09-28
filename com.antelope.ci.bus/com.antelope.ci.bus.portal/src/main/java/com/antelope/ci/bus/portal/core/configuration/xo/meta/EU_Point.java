// com.antelope.ci.bus.portal.configuration.xo.Point.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.meta;

import java.io.Serializable;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * xml extention point 
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-17		下午4:31:43 
 */
public enum EU_Point implements Serializable {
	BASE("base"),
	ACTION("action"),
	LAYOUT("layout"),
	PARTS("parts");
	
	private String name;
	private EU_Point(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public static EU_Point toPoint(String name) throws CIBusException {
		for (EU_Point p : EU_Point.values()) {
			if (p.getName().equalsIgnoreCase(name))
				return p;
		}
		
		throw new CIBusException("", "unknow point name");
	}
}

