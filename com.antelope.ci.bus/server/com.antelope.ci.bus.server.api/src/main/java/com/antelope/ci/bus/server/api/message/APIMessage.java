// com.antelope.ci.bus.server.api.APIMessage.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.message;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		下午3:48:25 
 */
public class APIMessage extends APIHeader {
	protected byte[] message;
	
	public APIMessage() {
		super();
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
		bl = message.length;
	}
	
	public int size() {
		return APIHeader.SIZE + (int) bl;
	}
}

