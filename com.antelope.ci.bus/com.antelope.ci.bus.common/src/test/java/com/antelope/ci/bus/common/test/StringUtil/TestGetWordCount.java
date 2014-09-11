// com.antelope.ci.bus.common.test.TestGetWordCount.java
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
 * @Date	 2014-2-8		下午2:52:14 
 */
public class TestGetWordCount extends TestCase {
	private static final String chinese_str = "测试"; 
	private static final String chinese_str1 = "/p:分页 /s:查询"; 
	
	@Test
	public void test() {
		test_case(chinese_str);
		test_case(chinese_str1);
	}
	
	private void test_case(String s) {
		System.out.println(s + " count is " + StringUtil.lengthVT(s));
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestGetWordCount.class);
	}
}

