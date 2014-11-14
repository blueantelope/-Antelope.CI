// com.antelope.ci.bus.server.shell.PortalShellText.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell;

import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.ShellText;


/**
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年9月22日		上午11:21:11 
 */
public class PortalShellText {
	private static final String FOCUS_PREFIX = "<focus>";
	private static final String FOCUS_SUFFIX = "</focus>";
	private static final String BLOCK_PREFIX = "<block";
	private static final String BLOCK_SUFFIX= "</block>";
	
	public static boolean isFocus(String str) {
		return StringUtil.contain(str, FOCUS_PREFIX, FOCUS_SUFFIX);
	}
	
	public static List<String> splitForP(String str) {
		List<String> splitList = new ArrayList<String>();
		splitForP(splitList, str, 0);
		
		return splitList;
	}
	
	private static void splitForP(List<String> splitList, String str, int from_index) {
		if (from_index > str.length())
			return;
		
		int start_index = -1;
		int end_index = -1;
		int suffixLength = -1;
		int text_start_index = str.indexOf(ShellText.TEXT_PREFIX, from_index);
		int block_start_index = str.indexOf(BLOCK_PREFIX, from_index);
		if (text_start_index == -1) {
			start_index = block_start_index;
			suffixLength = BLOCK_SUFFIX.length();
			end_index = str.indexOf(BLOCK_SUFFIX, from_index);
		} else if (block_start_index == -1) {
			start_index = text_start_index;
			suffixLength = ShellText.TEXT_SUFFIX.length();
			end_index = str.indexOf(ShellText.TEXT_SUFFIX, from_index);
		} else {
			if (text_start_index < block_start_index) {
				start_index = text_start_index;
				end_index = str.indexOf(ShellText.TEXT_SUFFIX, from_index);
				suffixLength = ShellText.TEXT_SUFFIX.length();
			} else {
				start_index = block_start_index;
				end_index = str.indexOf(BLOCK_SUFFIX, from_index);
				suffixLength = BLOCK_SUFFIX.length();
			}
		}
		
		if (start_index != -1 && end_index > start_index) {
			if (start_index > from_index)
				ShellText.strToSplitList(splitList, str, from_index, start_index);
			splitList.add(str.substring(start_index, end_index+suffixLength));
			from_index = end_index + suffixLength;
			splitForP(splitList, str, from_index);
		} else {
			ShellText.strToSplitList(splitList, str, from_index, str.length());
		}
	}
	
	public static boolean containBlock(String str) {
		return StringUtil.containEndsite(str, BLOCK_PREFIX, BLOCK_SUFFIX);
	}
	
	public static String genBlockText(String str, String name) {
		return BLOCK_PREFIX + " name=\"" + name + "\">" + str + BLOCK_SUFFIX;
	}
	
	public static String genFocusText(String str) {
		return FOCUS_PREFIX + str + FOCUS_SUFFIX;
	}
	
	public static String peel(String str) {
		String ret = str;
		if (containBlock(str)) {
			ret = StringUtil.deleteFirst(ret, BLOCK_PREFIX + " name=\"", ">");
			ret = StringUtil.deleteLast(ret, BLOCK_SUFFIX); 
		}
		if (isFocus(ret))
			ret = StringUtil.peel(ret, FOCUS_PREFIX, FOCUS_SUFFIX);
		return ret;
	}
	
	public static boolean isCommonShellText(String str) {
		return ShellText.isShellText(peel(str));
	}
	
	public static String getName(String str) {
		return StringUtil.find(str, BLOCK_PREFIX + " name=\"", "\"");
	}
	
	public static String createNewText(String str, String replace) throws CIBusException {
		StringBuffer newText = new StringBuffer();
		boolean is_block = false;
		if (containBlock(str)) {
			is_block = true;
			newText.append(StringUtil.subString(str, BLOCK_PREFIX, ">"));
		}
		boolean is_focus = false;
		if (isFocus(str)) {
			is_focus = true;
			newText.append(FOCUS_PREFIX);
		}
		if (ShellText.isShellText(peel(str)))
			newText.append(ShellText.toShellText(str, replace));
		
		if (is_focus)
			newText.append(FOCUS_SUFFIX);
		if (is_block)
			newText.append(BLOCK_SUFFIX);
		
		return newText.toString();
	}
}