// com.antelope.ci.bus.vcs.git.test.TestServiceWithClone.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.git.test;

import org.junit.Test;

import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.access.vcs.result.BusVcsResult;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-23		上午12:02:36 
 */
public class TestServiceWithClone extends TestBaseGit {

	@Override
	protected void init() throws Exception {
		
		// TODO Auto-generated method stub
		
	}
	
	@Test
	public void test() throws CIBusException {
		FileUtil.delFolder(test_antelopeCI);
		model.setReposPath(test_antelopeCI);
		BusVcsResult result = gitService.clone(model);
		System.out.println(result.getResult());
		System.out.println(result.getMessage());
	}

	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestServiceWithClone.class);
	}
}
