// com.antelope.ci.bus.framework.TestClassPath.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.framework;

import java.net.URL;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * TODO 描述
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-8-29 下午1:54:40
 */
public class TestClassPath extends TestCase {
//	@Test
//	public void testJreExt() {
//		Properties props = System.getProperties();
//		for (Object k : props.keySet()) {
//			String key = k.toString();
//			if (key.contains("java")) {
//				System.out.println(key + "->" + props.getProperty(key));
//			}
//		}
//	}

	@Test
	public void testClassloader() {
		ClassLoader cl = this.getClass().getClassLoader();
		System.out.println(cl);
		ClassLoader extCl = cl.getParent();
	}

	@Test
	public void testPackage() throws Exception {
		Package[] pack = Package.getPackages();

		// print all packages, one by one
		for (int i = 0; i < pack.length; i++) {
			System.out.println("" + pack[i]);
		}

		Class c = Class.forName("javax.crypto.Cipher");
		URL u = c.getResource("");
		System.out.println(u);
//		Package p = c.getPackage();
//		URL url = p.getClass().getProtectionDomain().getCodeSource()
//				.getLocation();
//		System.out.println(url);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestClassPath.class);
	}
}
