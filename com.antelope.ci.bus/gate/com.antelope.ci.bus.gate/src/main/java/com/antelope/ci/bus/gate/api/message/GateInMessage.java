// com.antelope.ci.bus.gate.api.message.GateInMessage.java
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
 * @Date	 2015年3月25日		下午5:11:06 
 */
public class GateInMessage extends GateMessage {
	public GateInMessage() {
		super();
	}
	
	public GateInMessage(GateInMessage message) {
		super(message);
	}
}
