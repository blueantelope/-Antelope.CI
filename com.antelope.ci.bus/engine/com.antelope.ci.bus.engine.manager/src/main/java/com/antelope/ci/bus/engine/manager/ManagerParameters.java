// com.antelope.ci.bus.service.ServiceArguments.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 服务的参数属性
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-6-23		下午4:42:38 
 */
public class ManagerParameters {
	protected boolean autoLoad;		// 自动装载
	protected EU_ManagerState state;	// 状态
	protected int priority;	// 优先级
	
	public ManagerParameters() {
		super();
	}

	public ManagerParameters(boolean autoLoad, EU_ManagerState state,
			int priority) {
		super();
		this.autoLoad = autoLoad;
		this.state = state;
		this.priority = priority;
	}

	// getter and setter
	public void setAutoLoad(boolean autoLoad) {
		this.autoLoad = autoLoad;
	}
	public boolean isAutoLoad() {
		return this.autoLoad;
	}
	public boolean needLoad() {
		if (autoLoad && state == EU_ManagerState.UNACTIVE)
			return true;
		return false;
	}
	public boolean needUnload() {
		if (state == EU_ManagerState.UNLOAD)
			return true;
		return false;
	}

	public EU_ManagerState getState() {
		return state;
	}
	public void setState(EU_ManagerState state) {
		this.state = state;
	}
	public void setState(int state_code) throws CIBusException {
		this.state = EU_ManagerState.fromCode(state_code);
	}

	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
}