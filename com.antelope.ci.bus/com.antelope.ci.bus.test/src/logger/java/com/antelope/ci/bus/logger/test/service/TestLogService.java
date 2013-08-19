// com.antelope.ci.bus.logger.test.service.TestLogService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.logger.test.service;

import org.apache.log4j.Logger;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-8		下午4:09:09 
 */
public interface TestLogService {
	public Logger getLog4j(Class clazz);
}

