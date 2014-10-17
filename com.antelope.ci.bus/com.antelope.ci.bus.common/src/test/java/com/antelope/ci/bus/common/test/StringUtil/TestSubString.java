// com.antelope.ci.bus.common.test.StringUtil.TestSubString.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test.StringUtil;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-8		下午3:57:50 
 */
public class TestSubString extends TestCase {

	@Test
	public void test() throws CIBusException {
		String str = "sssss一sssss";
		String sub = StringUtil.subStringVT(str, 0, 7);
		System.out.println(sub);
		System.out.println(sub.getBytes().length);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestSubString.class);
	}
}

