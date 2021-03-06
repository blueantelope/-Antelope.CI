// com.antelope.ci.bus.portal.configuration.xo.test.TestCommon.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.test;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.StaleBusPortalConfigurationHelper;
import com.antelope.ci.bus.portal.core.configuration.xo.Portal;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-6		上午11:54:34 
 */
public abstract class TestCommon extends TestCase {
	protected Portal portal;
	
	@Before
	protected void setUp() throws CIBusException {
		StaleBusPortalConfigurationHelper.getHelper().init();
		portal = StaleBusPortalConfigurationHelper.getHelper().getPortal();
	}
	
	@Test
	public abstract void test();
}

