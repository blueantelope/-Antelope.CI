// com.antelope.ci.bus.logger.test.TestSystemPorpertis.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.logger.test;

import junit.framework.TestCase;

import org.junit.Test;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-7		上午11:10:21 
 */
public class TestSystemPorpertis extends TestCase {
	@Test
	public void testClasspath() {
		System.out.println(System.getProperty("java.ext.dirs"));
	}
}

