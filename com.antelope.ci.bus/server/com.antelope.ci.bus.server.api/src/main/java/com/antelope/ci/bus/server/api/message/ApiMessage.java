// com.antelope.ci.bus.server.api.APIMessage.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.message;

import com.antelope.ci.bus.common.exception.CIBusException;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		下午3:48:25 
 */
public class ApiMessage extends ApiHeader {
	protected byte[] body;
	
	public ApiMessage() {
		super();
	}
	
	public void init() {
		super.init();
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
		bl = body.length;
	}
	
	public int size() {
		return ApiHeader.HEADER_SIZE + (int) bl;
	}
	
	public byte[] toBytes() {
		byte[] message = new byte[size()];
		byte[] header = toHeaderBytes();
		System.arraycopy(header, 0, message, 0, HEADER_SIZE);
		System.arraycopy(body, 0, message, HEADER_SIZE, (int) bl);
		
		return message;
	}
	
	public void fromBytes(byte[] bs) throws CIBusException {
		super.fromHeaderBytes(bs);
		int bodyLen = (int) bl;
		if (bodyLen > 0) {
			body = new byte[bodyLen];
			System.arraycopy(body, 0, bs, HEADER_SIZE, bodyLen);
		}
	}
}
