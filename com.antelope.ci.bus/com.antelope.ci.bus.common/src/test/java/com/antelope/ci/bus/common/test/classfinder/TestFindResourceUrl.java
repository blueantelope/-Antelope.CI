// com.antelope.ci.bus.common.test.classfinder.TestFindResourceUrl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test.classfinder;

import java.io.IOException;
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
 * @Date	 2013-12-17		下午4:46:51 
 */
public class TestFindResourceUrl extends TestCase {
	@Test
	public void test() throws CIBusException, IOException {
		List<String> propsList = ClassFinder.getPropsResource("com.antelope.ci.bus.common.test", this.getClass().getClassLoader());
		for (String props : propsList) {
			System.out.println(props);
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestFindResourceUrl.class);
	}
}

