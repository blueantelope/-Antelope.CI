// com.antelope.ci.bus.common.test.resourceutil.TestClassNameToUrl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test.resourceutil;

import java.net.URL;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.ResourceUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-6		下午3:13:37 
 */
public class TestClassNameToUrl extends TestCase {
	@Test
	public void testRun() {
		try {
			URL url = ResourceUtil.classNameToUrl("sun.security.util.Debug");
			System.out.println(url);
		} catch (CIBusException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestClassNameToUrl.class);
	}
}
