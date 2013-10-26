// com.antelope.ci.bus.vcs.git.test.TestServiceWithList.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.vcs.git.test;

import org.junit.Test;

import com.antelope.ci.bus.common.FileNode;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.vcs.model.BusVcsListModel;
import com.antelope.ci.bus.vcs.model.BusVcsModel.AccessType;
import com.antelope.ci.bus.vcs.result.BusVcsListResult;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-22		下午11:08:28 
 */
public class TestServiceWithList extends TestBaseGit {
	private BusVcsListModel listModel;
	
	@Override
	protected void init() throws Exception {
		listModel = new BusVcsListModel();
		listModel.setInfo(super.model);
		listModel.setReposPath(test_antelopeCI);
	}
	
	@Test
	public void testForLocal() throws CIBusException {
		BusVcsListResult result = gitService.list(listModel);
		System.out.println("=========Local Begin=========");
		for (FileNode node : result.getNodeList()) {
			System.out.println(node.getPath());
		}
		System.out.println("=========Local End=========");
	}

	@Test
	public void testForRemote() throws CIBusException {
		BusVcsListModel listModel = new BusVcsListModel();
		listModel.setInfo(super.model);
		listModel.setReposPath(test_antelopeCI);
		listModel.setAccessType(AccessType.REMOTE);
		BusVcsListResult result = gitService.list(listModel);
		System.out.println("=========Remote Begin=========");
		for (FileNode node : result.getNodeList()) {
			System.out.println(node.getPath());
		}
		System.out.println("=========Remote End=========");
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestServiceWithList.class);
	}
}

