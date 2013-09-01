// com.antelope.ci.bus.common.test.TestResourceUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test;

import java.net.URL;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.ResourceUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 资源工具类 TestCase
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		下午12:47:42 
 */
public class TestResourceUtil extends TestCase {
	@Test
	public void testGetClassPath() throws CIBusException {
		String cp = ResourceUtil.getClassPath();
		System.out.println(cp);
	}
	
	@Test
	public void testGetJarParent() throws CIBusException {
		URL path = ResourceUtil.getJarParent();
		System.out.println("====testGetJarParent==== : " + path);
	}
	
	@Test
	public void testReadJarBus() throws CIBusException {
//		String jarPath = "D:\\data\\git\\@Antelope.CI\\com.antelope.ci.bus\\com.antelope.ci.bus.logger\\target\\com.antelope.ci.bus.logger-0.1.0.jar";
//		JarBusProperty busProperty = ResourceUtil.readJarBus(jarPath);
	}
	
	public void testGetClassName() {
		List<String> nameList = ResourceUtil.getClassName("javax.crypto");
		for (String name : nameList) {
			System.out.println(name);
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestResourceUtil.class);
	}
}

