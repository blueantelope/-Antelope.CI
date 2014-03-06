// com.antelope.ci.bus.portal.configuration.xo.test.TestMakePlacePartTree.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo.test;

import java.util.List;

import org.junit.Test;

import com.antelope.ci.bus.portal.configuration.xo.PlacePartTree;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-6		下午12:00:54 
 */
public class TestMakePlacePartTree extends TestCommon {

	@Override
	@Test
	public void test() {
		List<PlacePartTree> treeList = portal.makePlacePartTree();
		System.out.println(treeList);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestMakePlacePartTree.class);
	}
}

