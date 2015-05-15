// com.antelope.ci.bus.portal.core.service.ConfigurationService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.service;

import java.util.Properties;

import com.antelope.ci.bus.portal.BusPortalContext;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月10日		下午4:37:02 
 */
public class ConfigurationService {
	public static final String NAME = "com.antelope.ci.bus.portal.core.service.ConfigurationService";
	private Properties properties;
	
	public ConfigurationService(Properties properties) {
		this.properties = properties;
	}
	
	public long getStartWait() {
		return (Long) properties.get(BusPortalContext.START_WAIT);
	}
}

