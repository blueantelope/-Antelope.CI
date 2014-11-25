// com.antelope.ci.bus.portal.configuration.test.TestParse.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.portal.core.configuration.xo.Portal;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_LAYOUT;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-20		下午9:43:44 
 */
public class TestParse extends TestCase {

	@Test
	public void test() throws CIBusException {
		BusPortalConfigurationHelper.getHelper().init();
		Portal portal = BusPortalConfigurationHelper.getHelper().getPortal();
		System.out.println(portal.getPlaceMap().get(EU_LAYOUT.CENTER.getName()));
		System.out.println(portal);
		System.out.println(portal.getPartMap().get("help").getValue());
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestParse.class);
	}
}

