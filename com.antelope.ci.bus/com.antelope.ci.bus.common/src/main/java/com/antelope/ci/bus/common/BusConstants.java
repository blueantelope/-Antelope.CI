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
public class BusConstants {
	/* ci bus的根目录 */
	public static final String BUS_HOME						= "antelope.ci.bus.home";
	/* etc目录 */
	public static final String ETC_DIR 							= "antelope.ci.bus.etc.dir";
	/* log目录 */
	public static final String LOG_DIR 							= "antelope.ci.bus.log.dir";
	/* osgi system启动包目录 */
	public static final String SYSTEM_DIR						= "antelope.ci.bus.system.dir";
	/* osgi system启动扩展包目录 */
	public static final String SYSTEM_EXT_DIR				= "antelope.ci.bus.system.ext.dir";
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
	public static final String JAR_LOAD						= "load";
	public static final String JAR_START_LEVEL				= "start.level";
	public static final String JAR_LOADER_URL				= "loader.url";
	public static final String JAR_SERVICES					= "bus.load.services";
	
	/* CI BUS中bundle加载的url集合 */
	public static final String BUS_BUNDLE_URLS			= "bus.bundle.urls";
	/* CI BUS bundle自定义属性 */
	public static final String BUS_BUNDLE_PROPS			= "bus.bundle.props";
}

