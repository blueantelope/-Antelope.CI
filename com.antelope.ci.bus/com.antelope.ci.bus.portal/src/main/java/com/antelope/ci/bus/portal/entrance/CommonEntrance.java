// com.antelope.ci.bus.portal.entrance.CommonEntrance.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.entrance;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-17		下午4:09:19 
 */
public abstract class CommonEntrance implements Entrance {
	@Override
	public void mount() throws CIBusException {
		beforeMount();
		init();
		afterMount();
	}
	
	@Override
	public void unmount() throws CIBusException {
		beforeUnmount();
		destroy();
		afterUnmount();
	}
	
	private void init() {
		
	}
	
	private void destroy() {
		
	}
	
	protected abstract void beforeMount() throws CIBusException;
	
	protected abstract void afterMount() throws CIBusException;
	
	
	protected abstract void beforeUnmount() throws CIBusException;
	
	protected abstract void afterUnmount() throws CIBusException;
}

