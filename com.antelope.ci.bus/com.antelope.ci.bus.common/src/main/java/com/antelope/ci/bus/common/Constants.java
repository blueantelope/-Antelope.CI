// com.antelope.ci.bus.Constants.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;


/**
 * 常量设置
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		下午12:24:31 
 */
public class Constants {
	/* ci bus的根目录 */
	public static final String BUS_HOME						= "antelope.ci.bus.home";
	/* etc目录 */
	public static final String ETC_DIR 							= "antelope.ci.bus.etc.dir";
	/* osgi bundle包目录 */
	public static final String SYSTEM_DIR						= "antelope.ci.bus.system.dir";
	/* 系统jar目录 */
	public static final String LIB_DIR							= "lantelope.ci.bus.ib.dir";
	/* 系统扩展jar目录 */
	public static final String LIB_EXT_DIR						= "antelope.ci.bus.lib.ext.dir";
	/* 运行时缓存目录 */
	public static final String CACHE_DIR						= "antelope.ci.bus.cache.dir";
	/* osgi plugin运行时包目录 */
	public static final String PLUGIN_DIR						= "antelope.ci.bus.plugin.dir";
	/* 日志配置文件 */
	public static final String LOG_CNF							=	"antelope.ci.bus.log.cnf";
	
	/* 
	 * bus ci jar中定义的bus.properties配置项 
	 */
	public static final String JAR_LOAD						= "antelope.ci.bus.jar.load";
	public static final String JAR_START_LEVEL				= "antelope.ci.bus.jar.start.level";
}

