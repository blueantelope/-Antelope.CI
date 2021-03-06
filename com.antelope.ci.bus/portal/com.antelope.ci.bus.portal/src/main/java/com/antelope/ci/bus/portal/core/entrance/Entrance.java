// com.antelope.ci.bus.portal.entrance.Entrance.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.entrance;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-18		下午10:10:04 
 */
public interface Entrance {
	public void init(Object... args) throws CIBusException;
	
	public void mount() throws CIBusException;
	
	public void unmount() throws CIBusException;
}

