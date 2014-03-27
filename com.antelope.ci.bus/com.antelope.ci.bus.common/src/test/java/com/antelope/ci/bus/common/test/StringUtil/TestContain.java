// com.antelope.ci.bus.common.test.StringUtil.TestContain.java
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
 * @Date	 2014-3-27		下午5:25:27 
 */
public class TestContain extends TestCase {
	
	@Test
	public void test() {
		String str = "${ssdfsdf}sdfsf";
		String prefix = "\\$\\{";
		String suffix = "\\}";
		boolean find = StringUtil.contain(str, prefix, suffix);
		System.out.println(find);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestContain.class);
	}
}

