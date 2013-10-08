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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.felix.framework.Felix;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;

import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.framework.test.TestUtils.JarEntryContent;



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
		super.setUp();
	}

	@Test
	public void test() throws BundleException, IOException  {
		Map params = new HashMap();
		params.put(Constants.FRAMEWORK_SYSTEMPACKAGES,
				"org.osgi.framework; version=1.4.0,"
						+ "org.osgi.service.packageadmin; version=1.2.0,"
						+ "org.osgi.service.startlevel; version=1.1.0,"
						+ "org.osgi.util.tracker; version=1.3.3,"
						+ "org.osgi.service.url; version=1.0.0,"
						+ "jre-1.6");
		params.put("org.osgi.framework.bootdelegation", " javax.xml.parsers, org.apache.log4j, org.apache.log4j.*");
		params.put(Constants.FRAMEWORK_BUNDLE_PARENT, "app");
		String cache = TestCreateBundleCache.class.getResource("/").getFile() + ".cache";
		File cache_file = new File(cache);
		if (cache_file.exists())
			FileUtil.delFolder(cache_file.toString());
		cache_file.mkdir();
		
		params.put("felix.cache.profiledir", cache);
		params.put("felix.cache.dir", cache);
		params.put(Constants.FRAMEWORK_STORAGE, cache);

		String manifest = "Bundle-SymbolicName: boot.test\n"
	            + "Bundle-Version: 1.1.0\n"
	            + "Bundle-ManifestVersion: 2\n"
	            + "Import-Package: org.osgi.framework\n";
		
		List<JarEntryContent> contentList = new ArrayList<JarEntryContent>();
		JarEntryContent content = new JarEntryContent(
				TestBunldeClassloader.class.getResource("log4j.properties"), 
				TestBunldeClassloader.class.getPackage().getName().replace('.', '/') + "/log4j.properties"
				);
		contentList.add(content);
		File bundleFile = TestUtils.createBundle(manifest, TestClassloaderActivator.class, contentList);

		Framework f = new Felix(params);
		f.init();
		f.start();

		final Bundle bundle = f.getBundleContext().installBundle(bundleFile.toURI().toString());
		bundle.start();
	}
	
	public static class TestClassloaderActivator implements BundleActivator {

		@Override
		public void start(BundleContext context) throws Exception {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			URL log4j_url = TestClassloaderActivator.class.getResource("log4j.properties");
			PropertyConfigurator.configure(log4j_url);
			Logger log = Logger.getLogger(TestClassloaderActivator.class);
			log.info("log4j started");
			System.out.println("start test activator");
		}

		@Override
		public void stop(BundleContext context) throws Exception {
			
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestBunldeClassloader.class);
	}
}

