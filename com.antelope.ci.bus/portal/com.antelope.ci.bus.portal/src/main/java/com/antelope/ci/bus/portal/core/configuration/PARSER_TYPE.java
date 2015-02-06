// com.antelope.ci.bus.portal.configuration.EU_ParseType.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-10		上午11:08:40 
 */
public enum PARSER_TYPE {
	DYNAMICAL("dynamic"),
	STATICAL("static");
	
	private String name;
	private PARSER_TYPE(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String toString() {
		return name;
	}
	
	public static PARSER_TYPE toType(String name) throws CIBusException {
		for (PARSER_TYPE t : PARSER_TYPE.values()) 
			if (t.getName().equalsIgnoreCase(name))
				return t;
		
		throw new CIBusException("", "unknown parser type");
	}
}
