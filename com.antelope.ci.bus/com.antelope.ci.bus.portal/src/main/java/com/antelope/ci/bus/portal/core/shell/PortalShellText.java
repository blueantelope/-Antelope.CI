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
	private static final String BLOCK_PREFIX = "<block>";
	private static final String BLOCK_SUFFIX= "</block>";
	
	public static boolean containBlock(String str) {
		if (StringUtil.empty(str))
			return false;
		str = str.trim();
		if (StringUtil.startsWithIgnoreCase(str, BLOCK_PREFIX) && str.endsWith(BLOCK_SUFFIX))
			return true;
		return false;
	}
	
	public static String genShelText(String str) {
		return BLOCK_PREFIX + str + BLOCK_SUFFIX;
	}
	
	public static String peel(String str) {
		String ret = str;
		if (containBlock(str)) {
			ret = StringUtil.deleteFirst(str, BLOCK_PREFIX);
			ret = StringUtil.deleteLast(ret, BLOCK_SUFFIX); 
		}
		
		return ret;
	}
	
	public static boolean isCommonShellText(String str) {
		return ShellText.isShellText(peel(str));
	}
}

