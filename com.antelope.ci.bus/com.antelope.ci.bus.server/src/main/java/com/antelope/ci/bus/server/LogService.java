// com.antelope.ci.bus.server.LogService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.log4j.Logger;

import com.antelope.ci.bus.logger.service.BusLogService;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-8		下午5:53:14 
 */
@Component(name="LogService", immediate=true)
public class LogService {
	@Requires
	private static BusLogService busLog; // Service Dependency

	public static Logger getLogger(Class clazz) {
		return busLog.getLog4j(clazz);
	}
}

