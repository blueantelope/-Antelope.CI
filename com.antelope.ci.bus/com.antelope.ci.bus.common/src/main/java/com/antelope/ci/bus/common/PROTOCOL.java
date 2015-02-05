// com.antelope.ci.bus.common.PROTOCOL.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月5日		上午11:07:56 
 */
public enum PROTOCOL {
	UDP("udp"),
	TCP("tcp");
	
	private String name;
	
	private PROTOCOL(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}

