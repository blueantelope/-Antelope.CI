// com.antelope.ci.bus.portal.configuration.xo.EU_Embed.java
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
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-17		下午5:02:52 
 */
public enum EU_Embed implements Serializable {
	REPLACE("replace"),
	APPEND("append");
	
	private String name;
	private EU_Embed(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public static EU_Embed toEmbed(String name) throws CIBusException {
		for (EU_Embed e : EU_Embed.values()) {
			if (e.getName().equalsIgnoreCase(name))
				return e;
		}
		
		throw new CIBusException("", "unknow embed name");
	}
}

