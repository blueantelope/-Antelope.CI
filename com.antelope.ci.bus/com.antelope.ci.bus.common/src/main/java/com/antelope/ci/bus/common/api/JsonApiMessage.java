// com.antelope.ci.bus.common.api.JsonApiMessage.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.api;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月21日		上午10:31:50 
 */
public class JsonApiMessage extends ApiMessage {
	public JsonApiMessage() {
		super();
	}
	
	public JsonApiMessage(JsonApiMessage message) {
		super(message);
	}
	
	@Override
	public void init() {
		super.init();
		bt = BT._json; // json
	}
	
	// immutable setter method
	@Override public void setBt(short bt) { }
}
