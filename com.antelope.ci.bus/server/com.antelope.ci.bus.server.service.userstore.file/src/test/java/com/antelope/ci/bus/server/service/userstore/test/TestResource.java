// com.antelope.ci.bus.server.service.userstore.test.TestResource.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service.userstore.test;

import java.net.URL;

import junit.framework.TestCase;

import org.junit.Test;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-8		下午3:09:43 
 */
public class TestResource extends TestCase {

	@Test
	public void testForUrl() {
		URL u = TestResource.class.getResource("/META-INF/bus.properties");
		System.out.println(u.toString());
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestResource.class);
	}
}

