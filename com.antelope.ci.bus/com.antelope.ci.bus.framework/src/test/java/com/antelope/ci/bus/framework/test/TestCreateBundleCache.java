// com.antelope.ci.bus.framework.test.TestBundleCache.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.framework.test;

import org.junit.Test;


/**
 * test unit for bundle cache using felix
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-29		下午9:21:21 
 */
public class TestCreateBundleCache extends TestBase {
	@Test
	public void test() throws Exception {
		m_archive = m_cache.create(1, 1, location, null);
		m_archive.closeAndDelete();
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestCreateBundleCache.class);
	}

}

