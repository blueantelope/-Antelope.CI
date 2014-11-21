// com.antelope.ci.bus.vcs.git.test.TestServiceWithDir.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.git.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.antelope.ci.bus.engine.access.vcs.model.BusVcsDiffModel;
import com.antelope.ci.bus.engine.access.vcs.result.BusVcsDiffResult;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-26		下午5:46:49 
 */
public class TestServiceWithDiff extends TestBaseGit {
	private BusVcsDiffModel diffModel ;

	@Override
	protected void init() throws Exception {
		diffModel = new BusVcsDiffModel();
		diffModel.setInfo(super.model);
	}

	@Test
	public void test() {
		List<String> pathList = new ArrayList<String>();
		pathList.add("README.md");
		pathList.add("README.md");
		diffModel.setPathList(pathList);
		BusVcsDiffResult result = gitService.diff(diffModel);
		System.out.println(result.getContent());
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestServiceWithDiff.class);
	}
}

