// com.antelope.ci.bus.common.JarBusProperty.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;


/**
 * bus.properties中的定义
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-2		下午6:20:01 
 */
public class JarBusProperty {
	private JarLoadMethod load;
	private int startLevel;
	
	// getter and setter
	public JarLoadMethod getLoad() {
		return load;
	}
	public void setLoad(JarLoadMethod load) {
		this.load = load;
	}
	/**
	 * 由装载方式的字符转换为enum表示，
	 * 无匹配时，使用IGNORE，即没有装载动作
	 * @param  @param loadValue
	 * @return void
	 * @throws
	 */
	public void setLoad(String loadValue) {
		this.load = JarLoadMethod.toLoadMethod(loadValue);
		if (null == this.load)
			this.load = JarLoadMethod.IGNORE;
	}
	public int getStartLevel() {
		return startLevel;
	}
	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
	}
}

