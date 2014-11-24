// com.antelope.ci.bus.vcs.git.test.TestServiceWithStatus.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.git.test;

import org.junit.Test;

import com.antelope.ci.bus.engine.model.vcs.input.BusVcsStatusModel;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsStatusResult;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-27		上午12:43:08 
 */
public class TestServiceWithStatus extends TestBaseGit {
	private BusVcsStatusModel statusModel ;

	@Override
	protected void init() throws Exception {
		statusModel = new BusVcsStatusModel();
		statusModel.setInfo(super.model);
	}

	@Test
	public void test() {
		BusVcsStatusResult result = gitService.status(statusModel);
		System.out.println("============added============");
		for (String add : result.getAddList()) {
			System.out.println(add);
		}
		System.out.println("============deleted============");
		for (String delete : result.getDeleteList()) {
			System.out.println(delete);
		}
		System.out.println("============changed============");
		for (String change : result.getChangeList()) {
			System.out.println(change);
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestServiceWithStatus.class);
	}
}