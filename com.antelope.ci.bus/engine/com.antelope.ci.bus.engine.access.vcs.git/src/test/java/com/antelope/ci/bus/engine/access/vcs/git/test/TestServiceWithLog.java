// com.antelope.ci.bus.vcs.git.test.TestServiceWithLog.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.git.test;

import org.junit.Test;

import com.antelope.ci.bus.engine.model.vcs.input.BusVcsLogModel;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsLogResult;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsLogResult.BusVcsLogResultInfo;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-26		下午11:00:16 
 */
public class TestServiceWithLog extends TestBaseGit {
	private BusVcsLogModel logModel ;

	@Override
	protected void init() throws Exception {
		logModel = new BusVcsLogModel();
		logModel.setInfo(super.model);
	}

	@Test
	public void test() {
		BusVcsLogResult result = gitService.log(logModel);
		for (BusVcsLogResultInfo log : result.getLogList()) {
			System.out.println(log.toString());
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestServiceWithLog.class);
	}
}

