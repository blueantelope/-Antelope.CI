// com.antelope.ci.bus.portal.configuration.EU_ParseType.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-10		上午11:08:40 
 */
public enum EU_ParseType {
	DYNAMICAL("dynamic"),
	STATICAL("static");
	
	private String name;
	private EU_ParseType(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String toString() {
		return name;
	}
	
	public static EU_ParseType toType(String name) throws CIBusException {
		for (EU_ParseType t : EU_ParseType.values()) 
			if (t.getName().equalsIgnoreCase(name))
				return t;
		
		throw new CIBusException("", "unknown parse type");
	}
}

