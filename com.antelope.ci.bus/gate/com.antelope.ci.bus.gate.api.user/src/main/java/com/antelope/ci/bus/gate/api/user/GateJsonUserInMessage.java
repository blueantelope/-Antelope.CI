// com.antelope.ci.bus.gate.service.user.GateJsonUserInMessage.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api.user;

import com.antelope.ci.bus.gate.api.message.GateJsonInMessage;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月23日		下午3:46:40 
 */
public class GateJsonUserInMessage extends GateJsonInMessage {
	public GateJsonUserInMessage() {
		super();
	}
	
	public GateJsonUserInMessage(GateJsonUserInMessage message) {
		super(message);
	}
	
	@Override
	public void init() {
		super.init();
		oc = 0x01;
	}
	
	// immutable setter method
	@Override public void setOc(int oc) { }
}
