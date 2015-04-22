// com.antelope.ci.bus.connector.gate.api.test.TestUserGateApiWithSsh.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.connector.gate.api.test;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.antelope.ci.bus.common.api.ApiHeader;
import com.antelope.ci.bus.common.api.ApiMessage;
import com.antelope.ci.bus.common.api.BT;
import com.antelope.ci.bus.common.api.OT;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.connector.SshAttribute;
import com.antelope.ci.bus.connector.gate.api.UserGateApiWithSsh;
import com.antelope.ci.bus.engine.model.user.User;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月17日		下午3:15:16 
 */
public class TestUserGateApiWithSsh extends TestCase {
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 9428;
	private static final String USERNAME = "blueantelope";
	private static final String PASSWORD = "blueantelope";
	private static final long CLOSE_TIMEOUT = 3000;
	
	protected UserGateApiWithSsh userGateApi;
	
	@Before
	protected void setUp() {
		try {
			SshAttribute attribute = new SshAttribute();
			attribute.setHost(HOST);
			attribute.setPort(PORT);
			attribute.setUsername(USERNAME);
			attribute.setPassword(PASSWORD);
			attribute.setWaitForClose(CLOSE_TIMEOUT);
			userGateApi = new UserGateApiWithSsh();
			userGateApi.initConnector(attribute);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testGetUser() throws CIBusException {
		User user = new User();
		user.setUsername(USERNAME);
		ApiMessage message = user.getMessage();
		message.setOt(OT._ls);
		message.setBt(BT._json);
		userGateApi.getUser(user);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestUserGateApiWithSsh.class);
	}
}

