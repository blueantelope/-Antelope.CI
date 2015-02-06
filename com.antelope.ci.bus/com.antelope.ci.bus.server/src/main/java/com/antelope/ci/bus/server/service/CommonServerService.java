// com.antelope.ci.bus.server.service.CommonService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusActivator;


/**
 * common service, init log4j
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-8		下午12:21:38 
 */
public abstract class CommonServerService {
	protected static Logger log;
	
	public CommonServerService() {
		try {
			log = BusActivator.getLog4j(this.getClass());
		} catch (CIBusException e) {
			e.printStackTrace();
		}
	}
}

