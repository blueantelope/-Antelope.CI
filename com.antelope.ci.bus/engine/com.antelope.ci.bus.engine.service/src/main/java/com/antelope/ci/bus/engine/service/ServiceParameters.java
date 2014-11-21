// com.antelope.ci.bus.service.ServiceArguments.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.service;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 服务的参数属性
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-6-23		下午4:42:38 
 */
public class ServiceParameters {
	protected boolean autoLoad;				// 自动装载
	protected EU_ServiceState state;			// 状态
	protected int priority;						// 优先级
	
	public void setAutoLoad(boolean autoLoad) {
		this.autoLoad = autoLoad;
	}
	
	public boolean isAutoLoad() {
		return this.autoLoad;
	}

	public EU_ServiceState getState() {
		return state;
	}

	public void setState(EU_ServiceState state) {
		this.state = state;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public void setState(int state_code) throws CIBusException {
		this.state = EU_ServiceState.fromCode(state_code);
	}
	
	public boolean needUnload() {
		if (state == EU_ServiceState.UNLOAD)
			return true;
		return false;
	}
	
	public boolean needLoad() {
		if (autoLoad && state == EU_ServiceState.UNACTIVE)
			return true;
		return false;
	}
}

