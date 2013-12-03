// com.antelope.ci.bus.server.shell.command.test.TestHelpContent.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command.test;

import junit.framework.TestCase;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.command.HelpContent;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		下午2:49:39 
 */
public class TestHelpContent extends TestCase {

	public void test() throws CIBusException {
		HelpContent helpContent = HelpContent.getContent();
		System.out.println(helpContent.getContent("echo", "help"));
		System.out.println(helpContent.getContent("frame", "help"));
		
		System.out.println(helpContent.getEchoContent("help"));
		System.out.println(helpContent.getFrameContent("help"));
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestHelpContent.class);
	}
}



