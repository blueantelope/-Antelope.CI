// com.antelope.ci.bus.gate.api.IGateApi.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.gate.api.message.GateInMessage;
import com.antelope.ci.bus.gate.api.message.GateOutMessage;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月25日		下午5:08:56 
 */
public interface IGateApi {
	public GateOutMessage ls(GateInMessage in) throws CIBusException;
	
	public GateOutMessage add(GateInMessage in) throws CIBusException;
	
	public GateOutMessage rm(GateInMessage in) throws CIBusException;
	
	public GateOutMessage mod(GateInMessage in) throws CIBusException;
}
