// com.antelope.ci.bus.test.Test.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.FileUtil;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-20		下午9:15:04 
 */
public class TestBus extends TestCase {
	@Test
	public void testDeleteCahceDir() {
		String cache_dir = "/data/software/eclipse/workspace/@Antelope.CI/com.antelope.ci.bus/builder/app/com.antelope.ci.bus.logger/.cache";
		FileUtil.delFolder(cache_dir);
	}
}

