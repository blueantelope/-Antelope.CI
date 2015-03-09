// com.antelope.ci.bus.server.api.buffer.BusAPIBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.api.message.APIMessage;
import com.antelope.ci.bus.server.common.BusBuffer;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月9日		上午11:07:12 
 */
public class BusAPIBuffer extends BusBuffer {
	protected InputStream in;
	protected OutputStream out;
	
	public BusAPIBuffer() {
		super();
	}
	
	public BusAPIBuffer(InputStream in, OutputStream out) {
		super();
		this.in = in;
		this.out = out;
	}
	
	public BusAPIBuffer(InputStream in, OutputStream out, int bufSize) {
		super(bufSize);
		this.in =  in;
		this.out = out;
	}
	
	public void initIO(InputStream in, OutputStream out) {
		this.in =  in;
		this.out = out;
	}
	
	public void write(APIMessage message) {
		try {
			out.write(message.toBytes());
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public void read(APIMessage message) throws CIBusException {
		read();
		message.fromBytes(byteValue());
	}
	
	protected void read() {
		buffer.reset();
		byte[] bs = new byte[bufSize];
		try {
			int index = 0;
			while ((index=in.read(bs)) != -1)
				buffer.put(toCharArray(bs, index));
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	protected char[] toCharArray(byte[] src, int length) {
		char[] dst = new char[length] ;
		System.arraycopy(src, 0, dst, 0, length);
		return dst;
	}
}
