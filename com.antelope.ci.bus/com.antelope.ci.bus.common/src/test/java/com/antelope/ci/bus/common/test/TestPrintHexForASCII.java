// com.antelope.ci.bus.common.test.TestPrintHexForASCII.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.ASCII;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-22		下午3:16:29 
 */
public class TestPrintHexForASCII extends TestCase {
	@Test public void test() {
		ASCII.printHex();
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestPrintHexForASCII.class);
	}
}

