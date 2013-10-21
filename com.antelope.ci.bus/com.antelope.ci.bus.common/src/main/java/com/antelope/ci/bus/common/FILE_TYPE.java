// com.antelope.ci.bus.common.FILE_TYPE.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * file type : includes directory and file
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-21		下午10:21:34 
 */
public enum FILE_TYPE {
	DIRECTOTRY("directory"),
	FILE("file");
	
	private String name;
	private FILE_TYPE(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public static FILE_TYPE toType(String name) throws CIBusException {
		if (name != null && name.length() > 0) {
			for (FILE_TYPE type : FILE_TYPE.values()) {
				if (type.getName().equalsIgnoreCase(name.trim()))
					return type;
			}
			throw new CIBusException("", "unknow file type : " + name);
		}
		throw new CIBusException("", "name of file type is null");
	}
}

