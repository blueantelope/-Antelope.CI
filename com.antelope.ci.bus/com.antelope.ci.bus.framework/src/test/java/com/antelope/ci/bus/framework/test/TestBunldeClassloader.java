// com.antelope.ci.bus.framework.test.TestBunldeClassloader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.framework.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.framework.Felix;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;



/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-7		下午11:31:25 
 */
public class TestBunldeClassloader extends TestBase {
	
	@Before
	@Override
	protected void setUp() throws Exception {
	}

	@Test
	public void test() throws BundleException, IOException  {
		Map params = new HashMap();
		params.put(Constants.FRAMEWORK_SYSTEMPACKAGES,
				"org.osgi.framework; version=1.4.0,"
						+ "org.osgi.service.packageadmin; version=1.2.0,"
						+ "org.osgi.service.startlevel; version=1.1.0,"
						+ "org.osgi.util.tracker; version=1.3.3,"
						+ "org.osgi.service.url; version=1.0.0");
		params.put(Constants.FRAMEWORK_BUNDLE_PARENT, "framework");
		File cacheDir = File.createTempFile("felix-cache", ".dir");
		cacheDir.delete();
		cacheDir.mkdirs();
		String cache = cacheDir.getPath();
		params.put("felix.cache.profiledir", cache);
		params.put("felix.cache.dir", cache);
		params.put(Constants.FRAMEWORK_STORAGE, cache);

		String mf = "Bundle-SymbolicName: boot.test\n"
				+ "Bundle-Version: 1.1.0\n" + "Bundle-ManifestVersion: 2\n"
				+ "Import-Package: org.osgi.framework\n";
		File bundleFile = TestUtils.createBundle();

		Framework f = new Felix(params);
		f.init();
		f.start();

		final Bundle bundle = f.getBundleContext().installBundle(
				bundleFile.toURI().toString());
		bundle.start();
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestBunldeClassloader.class);
	}
}

