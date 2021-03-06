// com.antelope.ci.bus.service.EU_ServiceState.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * manager service状态枚举表示
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-6-23		下午4:57:16 
 */
public enum EU_ManagerState {
	UNACTIVE("unactive", -1),
	ACTIVE("active", 1),
	UNLOAD("unload", -2),
	LOAD("load", 2);
	
	private String name;
	private int code;
	private EU_ManagerState(String name, int code) {
		this.name = name;
		this.code = code;
	}
	
	public String getName() {
		return this.name;
	}
	public int getCode() {
		return this.code;
	}
	
	public static EU_ManagerState fromName(String name) throws CIBusException {
		for (EU_ManagerState state : EU_ManagerState.values()) {
			if (name.trim().equalsIgnoreCase(state.getName()) )
				return state;
		}
		
		throw new CIBusException("", "unknowed service state name");
	}
	
	public static EU_ManagerState fromCode(int code) throws CIBusException {
		for (EU_ManagerState state : EU_ManagerState.values()) {
			if (code == state.getCode())
				return state;
		}
		
		throw new CIBusException("", "unknowed service state code");
	}
}

