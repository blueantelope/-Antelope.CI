// com.antelope.ci.bus.vcs.git.test.TestServiceWithReadContent.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.vcs.git.test;

import org.junit.Test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.vcs.model.BusVcsContentModel;
import com.antelope.ci.bus.vcs.model.BusVcsModel.AccessType;
import com.antelope.ci.bus.vcs.result.BusVcsContentResult;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-24		下午12:50:06 
 */
public class TestServiceWithReadContent extends TestBaseGit {

	@Override
	protected void init() throws Exception {
		
		// TODO Auto-generated method stub
		
	}
	
	@Test
	public void testForLocal() throws CIBusException {
		System.out.println("=========Local Begin=========");
		BusVcsContentModel contentModel = new BusVcsContentModel();
		contentModel.setInfo(super.model);
		contentModel.setReposPath(test_antelopeCI);
		contentModel.setPath("com.antelope.ci.bus.test/src/server/pom.xml");
		BusVcsContentResult result = gitService.readContent(contentModel);
		System.out.println(result.getContent());
		System.out.println("=========Local End=========");
	}
	
	@Test
	public void testForRemote() throws CIBusException {
		System.out.println("=========Remote Begin=========");
		BusVcsContentModel contentModel = new BusVcsContentModel();
		contentModel.setInfo(super.model);
		contentModel.setReposPath(test_antelopeCI);
		contentModel.setPath("com.antelope.ci.bus.test/src/server/pom.xml");
		contentModel.setAccessType(AccessType.REMOTE);
		BusVcsContentResult result = gitService.readContent(contentModel);
		System.out.println(result.getContent());
		System.out.println("=========Remote End=========");
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestServiceWithReadContent.class);
	}
}


