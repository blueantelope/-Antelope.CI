// com.antelope.ci.bus.gate.GateInMessage.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api.message;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		下午3:42:33 
 */
public class GateJsonInMessage extends GateInMessage {
	public GateJsonInMessage() {
		super();
	}
	
	public GateJsonInMessage(GateJsonInMessage message) {
		super(message);
	}
	
	@Override
	public void init() {
		super.init();
		bt = 0x01;
	}
	
	// immutable setter method
	@Override public void setBt(short bt) { }
}
