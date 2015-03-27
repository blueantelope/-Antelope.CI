// com.antelope.ci.bus.gate.ssh.test.TestClient.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.ssh.test;

import com.antelope.ci.bus.gate.ssh.CommonGateSshClient;
import com.antelope.ci.bus.server.api.message.ApiMessage;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月23日		下午5:20:52 
 */
public class TestGateSshClient extends CommonGateSshClient {
	@Override
	public void testRun() throws Exception {
		inSender.write(new ApiMessage().getBytes());
		inSender.flush();
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestGateSshClient.class);
	}
}
