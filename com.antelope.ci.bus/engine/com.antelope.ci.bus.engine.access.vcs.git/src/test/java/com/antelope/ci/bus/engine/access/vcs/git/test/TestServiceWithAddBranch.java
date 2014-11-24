// com.antelope.ci.bus.vcs.git.test.TestSercieWithAddBranch.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.git.test;

import org.junit.Test;

import com.antelope.ci.bus.engine.model.vcs.input.BusVcsAddBranchModel;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsResult;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-27		下午8:33:45 
 */
public class TestServiceWithAddBranch extends TestBaseGit {
	private BusVcsAddBranchModel branchModel ;

	@Override
	protected void init() throws Exception {
		branchModel = new BusVcsAddBranchModel();
		branchModel.setInfo(super.model);
		branchModel.setName("test" + System.currentTimeMillis());
	}

	@Test
	public void test() {
		BusVcsResult result = gitService.addBranch(branchModel);
		System.out.println(result.getResult());
		System.out.println(result.getMessage());
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestServiceWithAddBranch.class);
	}
}