// com.antelope.ci.bus.framework.test.TestBundleCache.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.framework.test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.felix.framework.Logger;
import org.apache.felix.framework.cache.BundleCache;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Constants;


/**
 * test unit for bundle cache using felix
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-29		下午9:21:21 
 */
public class TestCreateBundleCache extends TestCase {
	private BundleCache m_cache;
	private String location;
	
	@Before
	protected void setUp() throws Exception {
		Logger logger = new Logger(); 
		Map configMap = new HashMap();
		URL root_url = TestCreateBundleCache.class.getResource("/");
		configMap.put(Constants.FRAMEWORK_STORAGE, root_url.getFile());
		location = TestCreateBundleCache.class.getResource("test.jar").toURI().toString();
		m_cache = new BundleCache(logger, configMap);
	}
	
	@Test
	public void test() throws Exception {
		m_cache.create(1, 1, location, null);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestCreateBundleCache.class);
	}

}

