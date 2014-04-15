// com.antelope.ci.bus.common.test.StringUtil.TestEqualsWithChar.java
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
 * @Date	 2014-4-15		下午4:19:50 
 */
public class TestEqualsIgnoreCaseWithChar extends TestCase {
	
	@Test
	public void test() {
		String src = "\t";
		String base = "\t";
		System.out.println((int) src.charAt(0));
		System.out.println(StringUtil.equalsIgnoreCaseWithChar(src, base));
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestEqualsIgnoreCaseWithChar.class);
	}

}

