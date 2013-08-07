// com.antelope.ci.bus.logger.service.BusLogService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.logger.service;

import org.apache.log4j.Logger;


/**
 * 日志服务对外接口
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-7		下午4:38:52 
 */
public interface BusLogService {
	/**
	 * 取得log4j
	 * @param  @param clazz
	 * @param  @return
	 * @return LoggerO
	 * @throws
	 */
	public Logger getLot4j(Class clazz);
}

