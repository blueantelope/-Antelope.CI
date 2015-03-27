// com.antelope.ci.bus.gate.api.user.test.TestUserGateApi.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api.user.test;

import com.antelope.ci.bus.gate.api.user.GateJsonUserInMessage;
import com.antelope.ci.bus.gate.ssh.CommonGateSshClient;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月27日		下午12:05:13 
 */
public class TestUserGateApiSshClient extends CommonGateSshClient {
	@Override
	public void testRun() throws Exception {
		noClose();
		inSender.write(createInMessage().getBytes());
		inSender.flush();
	}
	
	protected GateJsonUserInMessage createInMessage() {
		return new GateJsonUserInMessage();
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestUserGateApiSshClient.class);
	}
}
