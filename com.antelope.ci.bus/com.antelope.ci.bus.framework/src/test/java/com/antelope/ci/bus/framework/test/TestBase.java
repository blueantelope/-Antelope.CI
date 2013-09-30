// com.antelope.ci.bus.framework.test.TestBase.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.framework.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.Logger;
import org.apache.felix.framework.cache.BundleArchive;
import org.apache.felix.framework.cache.BundleCache;
import org.junit.Before;
import org.osgi.framework.Constants;

import com.antelope.ci.bus.common.FileUtil;


/**
 * 基础测试
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-30		下午4:10:10 
 */
public class TestBase extends TestCase {
	protected Felix felix;
	protected BundleCache m_cache;
	protected String location;
	protected BundleArchive  m_archive;
	protected Map m_config;
	
	@Before
	protected void setUp() throws Exception {
		Logger logger = new Logger(); 
		m_config = new HashMap();
		String cache = TestCreateBundleCache.class.getResource("/").getFile() + ".cache";
		File cache_file = new File(cache);
		if (cache_file.exists())
			FileUtil.delFolder(cache_file.toString());
		cache_file.mkdir();
		m_config.put(Constants.FRAMEWORK_STORAGE, cache);
		felix = new Felix(m_config);
		location = TestUtils.createBundle().toURI().toString();
		m_cache = new BundleCache(logger, m_config);
	}
}

