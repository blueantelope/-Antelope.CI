// com.antelope.ci.bus.common.BusUtils.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;


/**
 * 工具类
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-6-6		下午6:04:46 
 */
public class BusUtils {
	
	public static String getRootDir() {
		return System.getProperty("user.dir");
	}
}

