// com.antelope.ci.bus.engine.manager.project.realization.EngineProjectManagerActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager.project.realization;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.manager.EngineManagerPublisher;
import com.antelope.ci.bus.engine.manager.EnginePublishInfo;
import com.antelope.ci.bus.engine.manager.project.EngineProjectManagerActivator;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月30日		下午10:51:15 
 */
public class EngineProjectManagerRealizationActivator extends EngineProjectManagerActivator {
	@Override
	protected void addServices() throws CIBusException {
		EngineManagerPublisher.publish(m_context, 
				EnginePublishInfo.createServicePublish("com.antelope.ci.bus.engine.manager.project.realization"));
	}
}

