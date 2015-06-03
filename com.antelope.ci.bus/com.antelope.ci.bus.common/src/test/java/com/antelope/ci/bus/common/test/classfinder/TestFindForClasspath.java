// com.antelope.ci.bus.common.test.TestClassLoader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test.ClassFinder;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.ClassFinder;



/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-8		下午5:24:03 
 */
public class TestFindForClasspath extends TestCase {
	private static boolean first = true;
	
	public TestFindForClasspath() {
		if (!first)
			System.out.println("Construct");
		else
			first = false;
	}

	@Test
	public void test() throws Exception {
		ClassLoader cl = this.getClass().getClassLoader();
		List<String> clsList = ClassFinder.findClasspath("com.antelope.ci.bus.common.test", this.getClass().getClassLoader());
		for (String cls : clsList) {
			System.out.println(cls);
			if (cls.endsWith("TestFindClass")) {
				Class.forName(cls).newInstance();
			}
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestFindForClasspath.class);
	}
}

