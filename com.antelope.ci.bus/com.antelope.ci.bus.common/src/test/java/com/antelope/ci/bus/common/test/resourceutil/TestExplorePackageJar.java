// com.antelope.ci.bus.common.test.resourceutil.TestExploreJar.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test.resourceutil;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import junit.framework.TestCase;

import org.junit.Test;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-6		下午4:46:32 
 */
public class TestExplorePackageJar extends TestCase {
	@Test
	public void testExplorePackage() throws IOException {
		sun.misc.URLClassPath ucp = sun.misc.Launcher.getBootstrapClassPath();
	    	URL[] urls = ucp.getURLs();
	    	for (URL url : urls) {
	    		String path = url.getFile();
	    		if (path.endsWith("rt.jar")) {
	    			JarFile jarFile = new JarFile(path);
	    			readPackage(jarFile);
	    		}
	    	}
	}
	
	private void readPackage(JarFile jarFile) throws IOException {
		Enumeration e = jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry entry = (JarEntry) e.nextElement();
			if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
				continue;
			}
			String className = entry.getName().substring(0, entry.getName().length() - 6);
			className = className.replace('/', '.');
			if (className.contains(".")) {
				String packageName = className.substring(0, className.lastIndexOf("."));
				System.out.println(packageName);
			}
			
		}
	}
	
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestExplorePackageJar.class);
	}
}

