// com.antelope.ci.bus.common.JarLoadLevel.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;


/**
 * jar装载的方式
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-2		下午6:21:22 
 */
public enum JarLoadMethod {
	IGNORE("ignore"),
	INSTALL("install"), 
	START("start");
	
	private String value;
	private JarLoadMethod(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public String toString() {
		return value;
	}
	
	/**
	 * 由装载jar方法的字符串表示转换为enum
	 * @param  @param val
	 * @param  @return
	 * @return JarLoadMethod
	 * @throws
	 */
	public static JarLoadMethod toLoadMethod(String val) {
		for (JarLoadMethod method: JarLoadMethod.values()) {
			if (val.equalsIgnoreCase(method.getValue())) {
				return method;
			}
		}
		
		return null;
	}
}

