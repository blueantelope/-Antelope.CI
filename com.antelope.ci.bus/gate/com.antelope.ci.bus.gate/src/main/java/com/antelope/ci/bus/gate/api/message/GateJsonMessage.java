// com.antelope.ci.bus.gate.GateJsonMessage.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api.message;

import com.antelope.ci.bus.server.api.message.BT;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		下午4:06:07 
 */
public class GateJsonMessage extends GateMessage {
	public GateJsonMessage() {
		super();
	}
	
	@Override
	public void init() {
		super.init();
		bt = BT._json; // json
	}
	
	// immutable setter method
	@Override public void setBt(short bt) { }
}
