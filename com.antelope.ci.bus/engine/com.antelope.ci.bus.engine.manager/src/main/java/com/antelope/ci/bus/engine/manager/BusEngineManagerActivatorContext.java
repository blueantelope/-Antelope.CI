// com.antelope.ci.bus.engine.manager.BusEngineManagerActivatorContext.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager;

import com.antelope.ci.bus.common.PropertiesUtil;
import com.antelope.ci.bus.osgi.BusActivatorContext;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月14日		下午3:55:14 
 */
public class BusEngineManagerActivatorContext extends BusActivatorContext {
	static final String CONTEXT_CLAZZ = BusEngineManagerActivatorContext.class.getName();
	private static final String PUBLISH_PERIOD = "publish.period";
	private static final long DEF_PUBLISH_PERIOD = 1 * 1000;
	
	public long getPublishPeriod() {
		return PropertiesUtil.getLong(properties, PUBLISH_PERIOD, DEF_PUBLISH_PERIOD);
	}
}
