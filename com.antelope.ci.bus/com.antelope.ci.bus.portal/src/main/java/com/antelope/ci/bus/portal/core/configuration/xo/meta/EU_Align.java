// com.antelope.ci.bus.portal.configuration.xo.EU_Align.java
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
 * @Date	 2014-3-11		下午5:13:59 
 */
public enum EU_Align implements Serializable {
	LEFT("left"),
	CENTER("center"),
	RIGHT("right");
	
	private String name;
	private EU_Align(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static EU_Align toAlign(String name) throws CIBusException {
		for (EU_Align a : EU_Align.values())
			if (a.getName().equalsIgnoreCase(name))
				return a;
		
		throw new CIBusException("", "unknow align");
	}
}

