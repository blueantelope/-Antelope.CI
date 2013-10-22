// com.antelope.ci.bus.vcs.git.test.TestServiceWithLogin.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.vcs.git.test;

import org.junit.Test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.vcs.result.BusVcsResult;



/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-22		下午12:39:38 
 */
public class TestServiceWithLogin extends TestBaseGit {
	

	@Override
	protected void init() throws Exception {
		
		
	}

	@Test
	public void test() throws CIBusException {
		BusVcsResult result = gitService.connect(model);
		System.out.println(result.getResult());
		System.out.println(result.getMessage());
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestServiceWithLogin.class);
	}

}

