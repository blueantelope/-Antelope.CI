// com.antelope.ci.bus.server.service.userstore.test.TestFileUserStoreServiceImpl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service.userstore.test;

import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.server.service.UserStoreService;
import com.antelope.ci.bus.server.service.user.User;
import com.antelope.ci.bus.server.service.userstore.FileUserStoreServiceImpl;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-8		下午2:57:24 
 */
public class TestFileUserStoreServiceImpl extends TestCase {

	@Test
	public void test() {
		UserStoreService us = new FileUserStoreServiceImpl();
		Map<String, User> um = us.getUserMap();
		for (String un : um.keySet()) {
			User u = um.get(un);
			System.out.println(un + ", " + u.getAuth_type());
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestFileUserStoreServiceImpl.class);
	}
}

