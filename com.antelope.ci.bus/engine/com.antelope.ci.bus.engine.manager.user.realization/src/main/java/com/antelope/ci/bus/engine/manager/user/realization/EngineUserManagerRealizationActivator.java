// com.antelope.ci.bus.engine.manager.user.realization.EngineUserManagerRealizationActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager.user.realization;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.manager.BusEngineManagerActivator;
import com.antelope.ci.bus.engine.manager.EngineManagerPublisher;
import com.antelope.ci.bus.engine.manager.EnginePublishInfo;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月30日		下午2:37:19 
 */
public class EngineUserManagerRealizationActivator extends BusEngineManagerActivator {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.manager.BusEngineManagerActivator#publishServices()
	 */
	@Override
	protected void publishServices() throws CIBusException {
		EngineManagerPublisher.publish(bundle_context, 
				EnginePublishInfo.createServicePublish("com.antelope.ci.bus.engine.manager.user.realization"));
	}
}
