// com.antelope.ci.bus.server.service.CommonService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusActivator;


/**
 * common manager, template
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-8		下午12:21:38 
 */
public abstract class CommonEngineManager extends BusEngineManager {
	protected static final Logger log = Logger.getLogger(CommonEngineManager.class);
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.manager.BusEngineManager#regist(org.osgi.framework.BundleContext)
	 */
	@Override public void regist(BundleContext m_context) throws CIBusException {
		log.info("regist engine manager");
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.manager.BusEngineManager#unregist(org.osgi.framework.BundleContext)
	 */
	@Override public void unregist(BundleContext m_context) throws CIBusException {
		log.info("unregiste engine manager");
	}
}