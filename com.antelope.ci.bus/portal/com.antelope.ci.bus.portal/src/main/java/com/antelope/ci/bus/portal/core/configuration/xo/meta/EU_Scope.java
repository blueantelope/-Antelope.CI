// com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Scope.java
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
 * @Date	 2014年9月28日		上午11:40:33 
 */
public enum EU_Scope {
	GLOBAL("global"),
	NATIVE("native");
	
	private String name;
	private EU_Scope(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public @Override String toString() {
		return name;
	}
	
	public static EU_Scope toScope(String name) throws CIBusException {
		for (EU_Scope scope : EU_Scope.values()) {
			if (scope.getName().equalsIgnoreCase(name)) 
				return scope;
		}
		
		throw new CIBusException("unknown scope");
	}
}

