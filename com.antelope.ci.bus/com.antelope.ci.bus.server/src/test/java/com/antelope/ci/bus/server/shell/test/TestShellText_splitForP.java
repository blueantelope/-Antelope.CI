// com.antelope.ci.bus.server.shell.test.TestTextShell_splitForP.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.test;

import java.util.List;

import com.antelope.ci.bus.server.shell.ShellText;

import junit.framework.TestCase;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-31		下午12:07:01 
 */
public class TestShellText_splitForP extends TestCase {
	private static final String str = "<p>" +
			"<text font-size=\"2\" indent=\"-5\" font-style=\"1\" font-mark=\"3\">   dd   </text>" +
			"<text font-size=\"2\" indent=\"0\" font-style=\"1\" font-mark=\"3\">  tt    </text>" +
			"</p>";
	
	public void test() {
		List<String> sList = ShellText.splitForP(str);
		for (String s : sList) {
			System.out.println(s);
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestShellText_splitForP.class);
	}
}

