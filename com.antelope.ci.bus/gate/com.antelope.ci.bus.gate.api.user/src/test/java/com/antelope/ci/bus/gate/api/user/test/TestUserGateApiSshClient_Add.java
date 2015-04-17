// com.antelope.ci.bus.gate.api.user.test.TestUserGateApiSshClient_Add.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api.user.test;

import com.antelope.ci.bus.common.api.OT;
import com.antelope.ci.bus.gate.api.user.GateJsonUserInMessage;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月27日		下午5:35:51 
 */
public class TestUserGateApiSshClient_Add extends TestUserGateApiSshClient {
	protected GateJsonUserInMessage createInMessage() {
		GateJsonUserInMessage message = new GateJsonUserInMessage();
		message.setOt(OT._add);
		return message;
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestUserGateApiSshClient_Add.class);
	}
}
