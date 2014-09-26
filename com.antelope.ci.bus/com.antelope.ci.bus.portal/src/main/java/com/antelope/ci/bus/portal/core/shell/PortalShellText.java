// com.antelope.ci.bus.server.shell.PortalShellText.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.server.shell.ShellText;


/**
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年9月22日		上午11:21:11 
 */
public class PortalShellText {
	private static final String FOCUS_PREFIX = "<focus>";
	private static final String FOCUS_SUFFIX = "</focus>";
	private static final String BLOCK_PREFIX = "<block>";
	private static final String BLOCK_SUFFIX= "</block>";
	
	public static boolean isFocus(String str) {
		return StringUtil.contain(str, FOCUS_PREFIX, FOCUS_SUFFIX);
	}
	
	public static boolean containBlock(String str) {
		return StringUtil.containEndsite(str, BLOCK_PREFIX, BLOCK_SUFFIX);
	}
	
	public static String genBlockText(String str) {
		return BLOCK_PREFIX + str + BLOCK_SUFFIX;
	}
	
	public static String genFocusText(String str) {
		return FOCUS_PREFIX + str + FOCUS_SUFFIX;
	}
	
	public static String peel(String str) {
		String ret = str;
		if (containBlock(str))
			ret = StringUtil.peel(ret, BLOCK_PREFIX, BLOCK_SUFFIX);
		if (isFocus(ret))
			ret = StringUtil.peel(ret, FOCUS_PREFIX, FOCUS_SUFFIX);
		return ret;
	}
	
	public static boolean isCommonShellText(String str) {
		return ShellText.isShellText(peel(str));
	}
}

