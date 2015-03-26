// com.antelope.ci.bus.gate.GateMessage.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api.message;

import com.antelope.ci.bus.server.api.message.ApiMessage;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		下午3:56:17 
 */
public class GateMessage extends ApiMessage {
	public GateMessage() {
		super();
	}
	
	@Override
	public void init() {
		super.init();
		type = 0x01;
	}
	
	// immutable setter method
	@Override public void setEndian(short endian) { }
	@Override public void setVersion(short version) { }
}

