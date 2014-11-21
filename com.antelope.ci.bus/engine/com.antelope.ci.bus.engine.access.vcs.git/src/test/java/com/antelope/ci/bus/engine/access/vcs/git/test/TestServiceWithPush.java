// com.antelope.ci.bus.vcs.git.test.TestServiceWithPush.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.git.test;

import org.junit.Test;

import com.antelope.ci.bus.engine.access.vcs.model.BusVcsPushModel;
import com.antelope.ci.bus.engine.access.vcs.result.BusVcsResult;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-27		下午3:58:13 
 */
public class TestServiceWithPush extends TestBaseGit {
	private BusVcsPushModel pushModel ;

	@Override
	protected void init() throws Exception {
		pushModel = new BusVcsPushModel();
		pushModel.setInfo(super.model);
	}

	@Test
	public void test() {
		BusVcsResult result = gitService.push(pushModel);
		System.out.println(result.getResult());
		System.out.println(result.getMessage());
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestServiceWithPush.class);
	}
}
