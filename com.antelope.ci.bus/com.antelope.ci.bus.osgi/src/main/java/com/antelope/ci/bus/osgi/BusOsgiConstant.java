// com.antelope.ci.bus.osgi.BusOsgiConstants.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.osgi;


/**
 * constans, final
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-13		上午11:36:34 
 */
public class BusOsgiConstant {
	public static final String SERVICE_NAME = "servcie.name";
	public static final String SERVICE_CLASS_NAME = "service.class.name";
	
	// default value of bus.properties
	public static final boolean DEF_SERVER_SWITCHER = false;
	public static final String DEF_SERVER_HOST = "0.0.0.0";
	public static final int DEF_SERVER_PORT = 9426;
	public static final String DEF_KEY_TYPE = "fixed";
	public static final String DEF_KEY_NAME = "com.antelope.bus.ci.key";
	public static final String DEF_WELCOME_BANNER = "@ANTELOPE CI SYSTEM";
}

