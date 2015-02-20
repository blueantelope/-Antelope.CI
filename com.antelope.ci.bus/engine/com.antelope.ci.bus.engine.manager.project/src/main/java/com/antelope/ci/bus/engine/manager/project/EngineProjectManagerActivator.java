// com.antelope.ci.bus.engine.manager.project.EngineProjectManagerActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager.project;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.manager.BusEngineManagerActivator;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月30日		下午11:38:02 
 */
public class EngineProjectManagerActivator extends BusEngineManagerActivator {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.manager.BusEngineManagerActivator#publishServices()
	 */
	@Override
	protected void publishServices() throws CIBusException {
		// nothing
	}
}