// com.antelope.ci.bus.portal.configuration.test.TestExtentionParse.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.test;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.StaleBusPortalConfigurationHelper;
import com.antelope.ci.bus.portal.core.configuration.xo.Portal;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-17		下午5:36:47 
 */
public class TestExtentionParse extends TestCase {

	@Test
	public void test() throws CIBusException, IOException {
		StaleBusPortalConfigurationHelper helper = StaleBusPortalConfigurationHelper.getHelper();
		helper.init();
		System.out.println(helper.getPortal());
		Portal portal_ext = helper.parseExtention("com.antelope.ci.bus.portal.core.test");
		System.out.println(portal_ext);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestExtentionParse.class);
	}
}

