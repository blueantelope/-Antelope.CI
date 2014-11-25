// com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_ContentType.java
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
 * @Date	 2014-9-10		上午11:46:15 
 */
public enum EU_ContentType {
	TEXT("text"),
	BLOCK("block");
	
	private String name;
	private EU_ContentType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static EU_ContentType toType(String name) throws CIBusException {
		for (EU_ContentType c : EU_ContentType.values())
			if (c.getName().equalsIgnoreCase(name))
				return c;
		
		throw new CIBusException("", "unknow content type");
	}
}

