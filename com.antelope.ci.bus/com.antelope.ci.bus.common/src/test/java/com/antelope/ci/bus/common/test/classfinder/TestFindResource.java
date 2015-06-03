// com.antelope.ci.bus.common.test.classfinder.TestFindResource.java
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
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-17		下午4:43:23 
 */
public class TestFindResource extends TestCase {
	
	@Test
	public void test() throws CIBusException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		ClassLoader cl = this.getClass().getClassLoader();
		List<String> clsList = ClassFinder.findResource("com.antelope.ci.bus.common.test", this.getClass().getClassLoader());
		for (String cls : clsList) {
			System.out.println(cls);
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestFindResource.class);
	}
}

