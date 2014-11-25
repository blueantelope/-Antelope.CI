// com.antelope.ci.bus.portal.test.TestBusPortalServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.BusServer;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-8		下午1:43:56 
 */
public class TestPortalMain extends TestCase {

	@Test
	public void test() throws CIBusException {
		BusServer server = new TestBusPortalServer();
		server.start();
	}
	
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestPortalMain.class);
	}
}

