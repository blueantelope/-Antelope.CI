// com.antelope.ci.bus.server.service.CommonService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.service;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.CommonBusActivator;


/**
 * common service, template
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-8		下午12:21:38 
 */
public abstract class CommonEngineService implements BusEngineService {
	protected static Logger log;
	protected ServiceParameters parameters;
	
	public void setParameters(ServiceParameters parameters) {
		this.parameters = parameters;
	}

	public CommonEngineService() {
		try {
			log = CommonBusActivator.getLog4j(this.getClass());
		} catch (CIBusException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.service.BusEngineService#regist(org.osgi.framework.BundleContext)
	 */
	@Override public void regist(BundleContext m_context) throws CIBusException {
		log.info("regist engine service");
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.service.BusEngineService#unregist(org.osgi.framework.BundleContext)
	 */
	@Override public void unregist(BundleContext m_context) throws CIBusException {
		log.info("unregiste engine service");
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.service.BusEngineService#getParameters()
	 */
	@Override public ServiceParameters getParameters() {
		return parameters;
	}
}