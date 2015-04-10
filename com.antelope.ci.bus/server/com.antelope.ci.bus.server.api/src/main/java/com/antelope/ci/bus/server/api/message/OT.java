// com.antelope.ci.bus.server.api.message.OT.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.message;


/**
 * OT(operation type) define.
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月23日		上午11:10:02 
 */
public final class OT {
	/* list */
	public final static short _ls = 0x01;
	
	/* add */
	public final static short _add = 0x02;
	
	/* delete */
	public final static short _delete = 0x03;
	
	/* modify */
	public final static short _mod = 0x04;
}
