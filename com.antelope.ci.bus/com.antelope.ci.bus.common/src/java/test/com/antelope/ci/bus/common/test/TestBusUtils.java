// com.antelope.ci.bus.common.test.TestBusUtils.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.BusUtils;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-6-6		下午6:06:17 
 */
public class TestBusUtils extends TestCase {
	@Test
	public void testGetRootDir() {
		String root_dir = BusUtils.getRootDir();
		System.out.println(root_dir);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestBusUtils.class);
	}
}

