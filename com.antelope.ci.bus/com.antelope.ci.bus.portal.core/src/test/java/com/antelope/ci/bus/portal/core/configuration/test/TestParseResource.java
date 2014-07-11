// com.antelope.ci.bus.portal.configuration.test.TestParseResource.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.test;

import java.io.InputStream;

import junit.framework.TestCase;

import org.junit.Test;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-8		下午2:28:20 
 */
public class TestParseResource extends TestCase {

	@Test
	public void test() {
		InputStream in = this.getClass().getResourceAsStream("/com/antelope/ci/bus/portal/core/test/portal_test.xml");
		System.out.println(in);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestParseResource.class);
	}
}

