// com.antelope.ci.bus.server.service.test.TestFindClass.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service.test;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-12		上午10:43:12 
 */
public class TestFindClass extends TestCase {
	
	@Test
	public void test() throws CIBusException {
		List<String>  classList = ClassFinder.findClasspath("com.antelope.ci.bus.server.service");
		for (String s : classList) {
			System.out.println(s);
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestFindClass.class);
	}
}

