// com.antelope.ci.bus.portal.configuration.OIRGIN.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-3		下午11:26:20 
 */
public enum EU_ORIGIN {
	GLOBAL("global"),
	PART("part");
	
	private String name;
	private EU_ORIGIN(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public @Override String toString() {
		return name;
	}
	
	public static EU_ORIGIN toOrigin(String name) throws CIBusException {
		for (EU_ORIGIN origin : EU_ORIGIN.values()) {
			if (origin.getName().equalsIgnoreCase(name)) 
				return origin;
		}
		
		throw new CIBusException("unknow origin");
	}
}

