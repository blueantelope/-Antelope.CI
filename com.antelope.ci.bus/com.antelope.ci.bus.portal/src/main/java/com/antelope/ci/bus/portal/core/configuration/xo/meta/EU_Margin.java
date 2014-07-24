// com.antelope.ci.bus.portal.configuration.xo.EU_Margin.java
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
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-11		下午5:40:14 
 */
public enum EU_Margin implements Serializable {
	BEFORE("before"),
	AFTER("after");
	
	private String name;
	private EU_Margin(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static EU_Margin toMargin(String name) throws CIBusException {
		for (EU_Margin m : EU_Margin.values())
			if (m.getName().equalsIgnoreCase(name))
				return m;
		
		throw new CIBusException("", "unknown margin");
	}
}

