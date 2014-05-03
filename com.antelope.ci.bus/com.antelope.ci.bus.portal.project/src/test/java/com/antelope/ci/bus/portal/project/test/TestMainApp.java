// com.antelope.ci.bus.portal.project.test.TestMainApp.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project.test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.test.TestBusPortalServer;
import com.antelope.ci.bus.server.BusServer;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-5-1		下午12:58:41 
 */
public class TestMainApp {
	public static void main(String[] args) throws CIBusException {
		BusServer server = new TestBusPortalServer();
		server.start();
	}
}

