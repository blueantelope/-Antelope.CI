// com.antelope.ci.bus.common.ValidatorUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2014-9-1 下午4:07:22
 */
public class ValidatorUtil {
	// 验证数字输入
	public static boolean isNumber(String str) {
		String regex = "[+-]?[0-9]*(\\.[0-9]+)?";
		return match(regex, str);
	}
	
	// 验证非零的正整数
	public static boolean isPositiveInteger(String str) {
        String regex = "^\\+?[1-9][0-9]*$";
        return match(regex, str);
    }

	// 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
