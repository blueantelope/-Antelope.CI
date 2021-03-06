// com.antelope.ci.bus.common.test.StringUtil.TestFormatString.java
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


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-6		下午6:55:18 
 */
public class TestFormatString extends TestCase {
	private static final String STR = "_{20}//////";
	private static final String STR1 = "//////";
	private static final String STR2 = "_{20}*{10}/////";
	
	@Test public void test() {
		String fs = StringUtil.loopString(STR2, "{", "}");
		System.out.println(fs);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestFormatString.class);
	}
}

