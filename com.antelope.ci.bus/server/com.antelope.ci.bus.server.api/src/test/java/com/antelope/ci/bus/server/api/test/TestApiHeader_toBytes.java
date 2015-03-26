// com.antelope.ci.bus.server.api.test.TestAPIHeader_toBytes.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.StreamUtil;
import com.antelope.ci.bus.server.api.message.ApiHeader;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月5日		下午5:38:14 
 */
public class TestApiHeader_toBytes extends TestCase {

	@Test public void test() {
		ApiHeader header = new ApiHeader();
		byte[] bytes = header.getHeaderBytes();
		System.out.println(StreamUtil.toHex(bytes));
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestApiHeader_toBytes.class);
	}
}
