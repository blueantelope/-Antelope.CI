// com.antelope.ci.bus.server.iface.Launcher.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.common;

import com.antelope.ci.bus.common.exception.CIBusException;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		下午4:32:40 
 */
public abstract class BusLauncher {
	protected BusCondition condition;
	
	public BusLauncher() {
		super();
	}
	
	public BusLauncher(BusCondition condition) {
		super();
		this.condition = condition;
	}
	
	public void setCondition(BusCondition condition) {
		this.condition = condition;
	}
	
	public ClassLoader getClassLoader() {
		return condition.getClassLoader();
	}
	
	public abstract BusChannel launch(BusSession session) throws CIBusException;
}
