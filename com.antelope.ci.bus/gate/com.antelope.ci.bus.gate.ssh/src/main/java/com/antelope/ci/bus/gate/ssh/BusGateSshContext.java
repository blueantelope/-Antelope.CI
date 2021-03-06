// com.antelope.ci.bus.gate.ssh.BusGateSshContext.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.ssh;

import com.antelope.ci.bus.gate.api.service.GateService;
import com.antelope.ci.bus.server.BusServerContext;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月15日		下午5:23:35 
 */
public class BusGateSshContext extends BusServerContext {
	static final String CONTEXT_CLAZZ = BusGateSshContext.class.getName();
	
	@Override
	protected void init() {
		super.init();
		loadServiceList.add(GateService.NAME);
	}
}

