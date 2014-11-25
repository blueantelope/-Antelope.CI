// com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_BlockMode.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.meta;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-9-2		下午3:26:02 
 */
public enum EU_BlockMode {
	HORIZONTAL("horizontal"),
	VERTICAL("vertical");
	
	private String name;
	private EU_BlockMode(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static EU_BlockMode toMode(String name) throws CIBusException {
		for (EU_BlockMode b : EU_BlockMode.values())
			if (b.getName().equalsIgnoreCase(name))
				return b;
		
		throw new CIBusException("", "unknow block mode");
	}
}

