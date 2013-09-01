// com.antelope.ci.bus.framework.TestClassPath.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.framework;

import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Test;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-29		下午1:54:40 
 */
public class TestClassPath extends TestCase {
	@Test
	public void testJreExt() {
		Properties props = System.getProperties();
		for (Object k : props.keySet()) {
			String key = k.toString();
			if (key.contains("java")) {
				System.out.println(key + "->" + props.getProperty(key));
			}
		}
	}

	
	@Test
	public void testClassloader() {
		ClassLoader cl = this.getClass().getClassLoader();
		System.out.println(cl);
		ClassLoader extCl = cl.getParent();
	}
	
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestClassPath.class);
	}
}

