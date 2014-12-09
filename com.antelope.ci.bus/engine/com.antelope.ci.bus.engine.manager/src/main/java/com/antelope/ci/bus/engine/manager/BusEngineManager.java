// com.antelope.ci.bus.server.service.Service.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager;

import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 对外manager接口定义
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		下午12:41:51 
 */
public abstract class BusEngineManager {
	protected ManagerParameters parameters;
	
	public ManagerParameters getParameters() {
		return parameters;
	}
	
	public void setParameters(ManagerParameters parameters) {
		this.parameters = parameters;
	}
	
	public void active() {
		parameters.setState(EU_ManagerState.ACTIVE);
	}
	
	public void load() {
		parameters.setState(EU_ManagerState.LOAD);
	}
	
	public void unactive() {
		parameters.setState(EU_ManagerState.UNACTIVE);
	}
	
	public void unload() {
		parameters.setState(EU_ManagerState.UNLOAD);
	}
	
	/**
	 * 注册服务
	 * @param  @param m_context
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	public abstract void regist(BundleContext m_context) throws CIBusException;
	
	/**
	 * 卸载服务
	 * @param  @param m_context
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	public abstract void unregist(BundleContext m_context) throws CIBusException;
}