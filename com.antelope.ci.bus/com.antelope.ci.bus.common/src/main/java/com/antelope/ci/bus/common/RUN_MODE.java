// com.antelope.ci.bus.common.RUN_MODE.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;



/**
 * 运行模式
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-21		上午10:51:04 
 */
public enum RUN_MODE {
	DEV("dev", "开发模式"), // 开发中使用的运行模式，不会用到缓存
	APP("app", "应用模式"); // 实际的应用模式，拥有全部功能

	private String name; // 表示名称
	private String value; // 显示名称

	private RUN_MODE(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	/**
	 * 返回value (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return value;
	}

	/**
	 * 由给定的表示名称转换为运行模式
	 * 
	 * @param @param name
	 * @param @return
	 * @return RUN_MODE
	 * @throws
	 */
	public static RUN_MODE toMode(String name) {
		if (name != null && !"".equals(name)) {
			for (RUN_MODE mode : RUN_MODE.values()) {
				if (name.trim().equalsIgnoreCase(mode.getName())) {
					return mode;
				}
			}
		}

		return null;
	}
}

