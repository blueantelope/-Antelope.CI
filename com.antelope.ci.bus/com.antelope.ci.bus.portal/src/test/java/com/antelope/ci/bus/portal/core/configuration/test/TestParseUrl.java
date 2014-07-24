// com.antelope.ci.bus.portal.configuration.test.TestParseUrl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.test;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import org.junit.Test;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-10		上午10:48:03 
 */
public class TestParseUrl extends TestCase {
	
	@Test
	public void test() throws MalformedURLException {
		URL u = this.getClass().getResource("/com/antelope/ci/bus/portal/core/test/portal_test.xml");
		String un = u.toString();
		URL u1 = new URL(un);
		System.out.println(u);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestParseUrl.class);
	}
}

