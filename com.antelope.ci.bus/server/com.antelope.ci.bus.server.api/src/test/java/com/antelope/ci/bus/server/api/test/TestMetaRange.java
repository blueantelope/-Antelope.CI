// com.antelope.ci.bus.server.api.test.TestMetaRange.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.test;

import junit.framework.TestCase;

import org.junit.Test;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月5日		下午5:17:47 
 */
public class TestMetaRange extends TestCase {

	@Test public void test() {
		byte b = (byte) 127;
		System.out.println(b);
		short s = (short) 32767;
		System.out.println(s);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestMetaRange.class);
	}
}

