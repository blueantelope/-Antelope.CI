// com.antelope.ci.bus.vcs.git.test.TestServiceWithReadCat.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.git.test;

import org.junit.Test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.access.vcs.model.BusVcsCatModel;
import com.antelope.ci.bus.engine.access.vcs.model.BusVcsModel.AccessType;
import com.antelope.ci.bus.engine.access.vcs.result.BusVcsCatResult;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-24		下午12:50:06 
 */
public class TestServiceWithCat extends TestBaseGit {

	@Override
	protected void init() throws Exception {
		
		// TODO Auto-generated method stub
		
	}
	
	@Test
	public void testForLocal() throws CIBusException {
		System.out.println("=========Local Begin=========");
		BusVcsCatModel CatModel = new BusVcsCatModel();
		CatModel.setInfo(super.model);
		CatModel.setReposPath(test_antelopeCI);
		CatModel.setPath("com.antelope.ci.bus.test/src/server/pom.xml");
		BusVcsCatResult result = gitService.cat(CatModel);
		System.out.println(result.getContent());
		System.out.println("=========Local End=========");
	}
	
	@Test
	public void testForRemote() throws CIBusException {
		System.out.println("=========Remote Begin=========");
		BusVcsCatModel CatModel = new BusVcsCatModel();
		CatModel.setInfo(super.model);
		CatModel.setReposPath(test_antelopeCI);
		CatModel.setPath("com.antelope.ci.bus.test/src/server/pom.xml");
		CatModel.setAccessType(AccessType.REMOTE);
		BusVcsCatResult result = gitService.cat(CatModel);
		System.out.println(result.getContent());
		System.out.println("=========Remote End=========");
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestServiceWithCat.class);
	}
}


