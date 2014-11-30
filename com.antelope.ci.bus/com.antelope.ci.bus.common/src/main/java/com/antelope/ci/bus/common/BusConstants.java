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
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		下午12:24:31 
 */
public class BusConstants {
	/* ci bus的根目录 */
	public static final String BUS_HOME= "antelope.ci.bus.home";
	/* etc目录 */
	public static final String ETC_DIR= "antelope.ci.bus.etc.dir";
	/* log目录 */
	public static final String LOG_DIR= "antelope.ci.bus.log.dir";
	/* osgi system启动包目录 */
	public static final String SYSTEM_DIR= "antelope.ci.bus.system.dir";
	/* osgi system启动共享lib目录 */
	public static final String SYSTEM_LIB_DIR= "antelope.ci.bus.system.lib.dir";
	/* osgi system启动引擎包目录 */
	public static final String SYSTEM_ENGINE_DIR= "antelope.ci.bus.system.engine.dir";
	/* osgi system启动引擎包model目录 */
	public static final String SYSTEM_ENGINE_MODEL_DIR= "antelope.ci.bus.system.engine.model.dir";
	public final static String SYSTEM_ENGINE_MODEL_DIRNAME= "com.antelope.ci.bus.engine.model";
	/* osgi system启动引擎包access目录 */
	public static final String SYSTEM_ENGINE_ACCESS_DIR= "antelope.ci.bus.system.engine.access.dir";
	public final static String SYSTEM_ENGINE_ACCESS_DIRNAME= "com.antelope.ci.bus.engine.access";
	/* osgi system启动引擎包manager目录 */
	public static final String SYSTEM_ENGINE_MANAGER_DIR= "antelope.ci.bus.system.engine.manager.dir";
	public final static String SYSTEM_ENGINE_MANAGER_DIRNAME= "com.antelope.ci.bus.engine.manager";
	/* osgi system启动引擎包lib目录 */
	public static final String SYSTEM_ENGINE_LIB_DIR= "antelope.ci.bus.system.engine.lib.dir";
	public final static String SYSTEM_ENGINE_LIB_DIRNAME= "lib";
	/* osgi system启动扩展包目录 */
	public static final String SYSTEM_EXT_DIR= "antelope.ci.bus.system.ext.dir";
	/* osgi system启动扩展包server目录 */
	public static final String SYSTEM_EXT_SERVER_DIR= "antelope.ci.bus.system.ext.server.dir";
	public final static String SYSTEM_EXT_SERVER_DIRNAME= "com.antelope.ci.bus.server";
	/* osgi system启动扩展包service目录 */
	public static final String SYSTEM_EXT_SERVICE_DIR= "antelope.ci.bus.system.ext.service.dir";
	public final static String SYSTEM_EXT_SERVICE_DIRNAME= "com.antelope.ci.bus.service";
	/* osgi system启动扩展包portal目录 */
	public static final String SYSTEM_EXT_PORTAL_DIR= "antelope.ci.bus.system.ext.portal.dir";
	public final static String SYSTEM_EXT_PORTAL_DIRNAME= "com.antelope.ci.bus.portal";
	/* osgi system启动扩展包lib目录 */
	public static final String SYSTEM_EXT_LIB_DIR= "antelope.ci.bus.system.ext.lib.dir";
	public final static String SYSTEM_EXT_LIB_DIRNAME= "lib";
	/* 系统jar目录 */
	public static final String LIB_DIR= "antelope.ci.bus.lib.dir";
	/* 系统引擎jar目录 */
	public static final String LIB_ENGINE_DIR= "antelope.ci.bus.lib.engine.dir";
	/* 系统扩展jar目录 */
	public static final String LIB_EXT_DIR= "antelope.ci.bus.lib.ext.dir";
	/* 运行时缓存目录 */
	public static final String CACHE_DIR= "antelope.ci.bus.cache.dir";
	/* osgi plugin运行时包目录 */
	public static final String PLUGIN_DIR= "antelope.ci.bus.plugin.dir";
	/* 日志配置文件 */
	public static final String LOG_CNF= "antelope.ci.bus.log.cnf";
	
	/* 
	 * bus ci jar中定义的bus.properties配置项 
	 */
	public static final String JAR_LOAD= "load";
	public static final String JAR_START_LEVEL= "start.level";
	public static final String JAR_LOADER_URL= "loader.url";
	public static final String JAR_SERVICES= "bus.load.services";
	
	/* CI BUS中bundle加载的url集合 */
	public static final String BUS_BUNDLE_URLS= "bus.bundle.urls";
	/* CI BUS bundle自定义属性 */
	public static final String BUS_BUNDLE_PROPS= "bus.bundle.props";
	
	/* run mode, include app and dev */
	public static final String BUS_RUN_MODE= "bus.run.mode";
}

