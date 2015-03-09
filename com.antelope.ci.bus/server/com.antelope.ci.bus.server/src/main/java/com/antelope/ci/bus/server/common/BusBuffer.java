// com.antelope.ci.bus.server.BusBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.common;

import java.nio.CharBuffer;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月9日		上午11:20:40 
 */
public class BusBuffer {
	private static final int BUF_SIZE = 512;
	protected int bufSize;
	protected CharBuffer buffer;
	
	public BusBuffer() {
		init(BUF_SIZE);
	}
	
	public BusBuffer(int bufSize) {
		init(bufSize);
	}
	
	protected void init(int bufSize) {
		this.bufSize = bufSize;
		buffer = CharBuffer.allocate(bufSize);
	}
	
	public void reset() {
		buffer.clear();
	}
	
	public String value() {
		int mark = buffer.position();
		buffer.flip();
		String s = buffer.toString();
		buffer.position(mark);
		buffer.limit(buffer.capacity());
		return s;
	}
	
	public String value(int start, int end) {
		int position = buffer.position();
		int limit = buffer.limit();
		buffer.position(start);
		buffer.limit(end);
		String s = buffer.toString();
		buffer.position(position);
		buffer.limit(limit);
		return s;
	}
	
	public void remove(int index, int length) {
		char[] puts =  charValue(index+length);
		buffer.position(index);
		buffer.put(puts);
	}
	
	protected void copyValue(int index, char[] dst) {
		int current = buffer.position();
		buffer.position(index);
		buffer.get(dst);
		buffer.position(current);
	}
	
	public byte[] byteValue() {
		char[] cs = charValue();
		byte[] bs = new byte[cs.length];
		System.arraycopy(cs, 0, bs, 0, cs.length);
		return bs;
	}
	
	public char[] charValue() {
		int mark = buffer.position();
		buffer.flip();
		char[] v = buffer.array();
		buffer.position(mark);
		buffer.limit(buffer.capacity());
		return v;
	}
	
	protected char[] charValue(int index) {
		int current = buffer.position();
		char[] chs = new char[current - index];
		buffer.position(index);
		buffer.get(chs);
		buffer.position(current);
		return chs;
	}
	
	@Override public String toString() {
		return value();
	}
}
