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
	public void testGetClassName() {
		List<String> nameList = ResourceUtil.getClassName("sun.security.util");
		for (String name : nameList) {
			System.out.println(name);
		}
	}
	
	@Test
	public void testClassNameToUrl() {
		try {
			URL url = ResourceUtil.classNameToUrl("java.util.List");
			System.out.println(url);
		} catch (CIBusException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestResourceUtil.class);
	}
}

