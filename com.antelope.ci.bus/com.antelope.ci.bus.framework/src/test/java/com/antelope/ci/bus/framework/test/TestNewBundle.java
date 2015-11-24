// com.antelope.ci.bus.framework.test.TestRevise.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.framework.test;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.junit.Test;
import org.osgi.framework.Bundle;



/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-30		下午3:06:02 
 */
public class TestNewBundle extends TestCreateBundleCache {
	@Override
	@Test
	public void test() throws Exception {
		m_config.put(FelixConstants.FRAMEWORK_BUNDLECACHE_IMPL, m_cache);
		felix = new Felix(m_config);
		felix.init();
//		felix.start();
		super.test();
		Class clazz = Class.forName("org.apache.felix.framework.BundleImpl");
		Object[] args = new Object[] {felix, m_archive};
		Bundle bundle = (Bundle) TestUtils.newInstance(clazz, args);
		System.out.println(bundle);
	}
	
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestNewBundle.class);
	}
}

