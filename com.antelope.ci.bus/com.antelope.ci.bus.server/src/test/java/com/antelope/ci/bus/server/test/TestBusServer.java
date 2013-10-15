// com.antelope.ci.bus.server.test.TestServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.test;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.BusConstants;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.BusServer;
import com.antelope.ci.bus.server.BusServerConfig;


/**
 * 测试bus server, ssh server是否可用
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-30		下午11:24:39 
 */
public class TestBusServer extends TestCase {
	/**
	 * @throws CIBusException 
	 * 启动服务器测试
	 * @param  @throws IOException
	 * @return void
	 * @throws
	 */
	@Test
	public void testServer() throws IOException, CIBusException {
		System.setProperty(BusConstants.CACHE_DIR, "/data/temp");
		BusServer server = new BusServer();
		BusServerConfig config = new BusServerConfig();
		config.setPort(9427);
		server.setConfig(config);
		server.start();
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestBusServer.class);
	}
}

