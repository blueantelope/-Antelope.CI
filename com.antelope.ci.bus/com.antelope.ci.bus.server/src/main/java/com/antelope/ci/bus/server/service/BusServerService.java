// com.antelope.ci.bus.server.service.Service.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service;

import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 对外发部service接口定义
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		下午12:41:51 
 */
public interface BusServerService {
	/**
	 * 注册服务
	 * @param  @param m_context
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	public void register(BundleContext m_context) throws CIBusException;
}

