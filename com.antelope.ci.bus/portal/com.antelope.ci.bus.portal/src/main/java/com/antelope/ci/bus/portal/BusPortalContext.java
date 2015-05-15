// com.antelope.ci.bus.portal.BusPortalActivatorContext.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal;

import java.util.Properties;

import com.antelope.ci.bus.common.PropertiesUtil;
import com.antelope.ci.bus.osgi.BusContext;
import com.antelope.ci.bus.portal.core.service.ConfigurationService;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月15日		上午10:12:47 
 */
public class BusPortalContext extends BusContext {
	static final String CONTEXT_CLAZZ = BusPortalContext.class.getName();
	private static final String BANNER = "bus.portal.banner";
	private final static String PARSER = "bus.portal.parser";	
	private final static String DEF_PARSER = "static";
	public final static String START_WAIT = "bus.portal.start.wait";
	public final static long DEF_START_WAIT = 10 * 1000;
	private final static String FORM_COMMAND_WAIT = "bus.portal.form.command.wait";
	private final static long DEF_FORM_COMMAND_WAIT = 30 * 1000;
	
	private ConfigurationService configurationService;
	
	public String getPortalBanner() {
		return PropertiesUtil.getString(properties, BANNER, getBanner());
	}
	
	public String getParser() {
		return PropertiesUtil.getString(properties, PARSER, DEF_PARSER);
	}
	
	public long getStartWait() {
		return PropertiesUtil.getLong(properties, START_WAIT, DEF_START_WAIT);
	}
	
	public long getFormCommandWait() {
		return PropertiesUtil.getLong(properties, FORM_COMMAND_WAIT, DEF_FORM_COMMAND_WAIT);
	}
	
	ConfigurationService getConfigurationService() {
		if (configurationService == null) {
			Properties configProps = new Properties();
			configProps.put(START_WAIT, getStartWait());
			configurationService =  new ConfigurationService(configProps);
		}
		
		return configurationService;
	}
}

