// com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_ComponentType.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.meta;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-30		上午11:21:55 
 */
public enum EU_ComponentType {
	textfield("textfield"),
	combo("combo"),
	checkbox("checkbox"),
	radiobox("radiobox"),
	button("button");
	
	private String name;
	private EU_ComponentType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override public String toString() {
		return name;
	}
	
	public static EU_ComponentType fromName(String name) throws CIBusException {
		for (EU_ComponentType type : EU_ComponentType.values()) {
			if (name.equalsIgnoreCase(type.getName()))
				return type;
		}
		
		throw new CIBusException("", "unknown type name");
	}
}