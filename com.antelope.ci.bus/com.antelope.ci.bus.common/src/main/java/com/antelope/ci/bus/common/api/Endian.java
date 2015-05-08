// com.antelope.ci.bus.server.api.message.Order.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.api;


/**
 * byte order define.
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月23日		上午11:13:16 
 */
public final class Endian {
	/* native */
	public final static short _native = 0x01;
	
	/* little endian */
	public final static short _litttle = 0x02;
	
	/* big endian */
	public final static short _big = 0x03;
	
	/* network endian */
	public final static short _network = 0x04;
	
	public final static String value(short endian) {
		switch (endian) {
			case _native:
				return "native";
			case _litttle:
				return "little";
			case _big:
				return "big";
			case _network:
				return "network";
		}
		
		return "";
	}
}
