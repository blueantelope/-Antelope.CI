// com.antelope.ci.bus.common.test.StringUtil.TestGetLastName.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test.StringUtil;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.BusConstants;
import com.antelope.ci.bus.common.StringUtil;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年12月6日		下午2:11:08 
 */
public class TestGetLastName extends TestCase {
	@Test public void test() {
		System.out.println(
				StringUtil.getLastName(this.getClass().getName(), "\\" + BusConstants.DOT));
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestGetLastName.class);
	}
}

