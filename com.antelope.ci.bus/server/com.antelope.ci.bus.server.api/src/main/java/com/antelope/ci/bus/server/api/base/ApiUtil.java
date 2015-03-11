// com.antelope.ci.bus.server.api.APIUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.base;

import com.antelope.ci.bus.common.StreamUtil;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月5日		下午4:31:40 
 */
public class ApiUtil {
	public static void fill2Bytes(int value, byte[] bytes, int start) {
		System.arraycopy(StreamUtil.unsignedShortToByteArray(value), 0, bytes, start, 2);
	}
	
	public static void fill4Bytes(long value, byte[] bytes, int start) {
		System.arraycopy(StreamUtil.unsignedIntToByteArray(value), 0, bytes, start, 4);
	}
	
	public static void fillBytes(byte[] from, byte[] bytes, int start, int length) {
		System.arraycopy(from, 0, bytes, start, length);
	}
	
	public static int from2Bytes(byte[] bytes, int index) {
		return StreamUtil.toUnsignedShort(new byte[]{bytes[index], bytes[index+1]});
	}
	
	public static long from4Bytes(byte[] bytes, int index) {
		return StreamUtil.toUnsignedShort(new byte[]{bytes[index], bytes[index+1], bytes[index+2], bytes[index+3]});
	}
	
	public static byte[] fromBytes(byte[] bytes, int index, int length) {
		byte[] value = new byte[length];
		System.arraycopy(bytes, index, value, 0, length);
		return value;
	}
}

