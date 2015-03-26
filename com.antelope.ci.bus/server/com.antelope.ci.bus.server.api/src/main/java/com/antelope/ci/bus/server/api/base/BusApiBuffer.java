// com.antelope.ci.bus.server.api.buffer.BusAPIBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.base;

import java.nio.CharBuffer;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.api.message.ApiMessage;
import com.antelope.ci.bus.server.common.BusBuffer;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月9日		上午11:07:12 
 */
public class BusApiBuffer extends BusBuffer {
	protected ApiIO io;
	
	public BusApiBuffer(ApiIO io) {
		super();
		this.io = io;
	}
	
	public BusApiBuffer(ApiIO io, int bufSize) {
		super(bufSize);
		this.io = io;
	}
	
	public void write(ApiMessage message) {
		try {
			io.write(message.getBytes());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public ApiMessage readMessage() throws CIBusException {
		read();
		try {
			ApiMessage message = new ApiMessage();
			message.fromBytes(byteValue());
			return message;
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
			return null;
		}
	}
	
	protected void read() throws CIBusException {
		buffer.clear();
		byte[] bs = new byte[bufSize];
		int index = 0;
		if ((index=io.read(bs)) != -1)
			buffer.put(toCharArray(bs, index));
		int remain = io.available();
		if (buffer.position() == bufSize && remain > 0) {
			bufSize += remain;
			buffer = copy(bufSize);
			if ((index=io.read(bs)) != -1)
				buffer.put(toCharArray(bs, index));
		}
	}
	
	protected CharBuffer copy(int size) {
		CharBuffer copyBuffer = CharBuffer.allocate(size);
		copyBuffer.put(buffer);
		return copyBuffer;
	}
	
	protected char[] toCharArray(byte[] src, int length) {
		char[] dst = new char[length] ;
		int n = 0;
		while (n < length) {
			dst[n] = (char) src[n];
			n++;
		}
		
		return dst;
	}
}
