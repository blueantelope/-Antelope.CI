// com.antelope.ci.bus.common.StringUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import java.net.URL;
import java.util.List;


/**
 * 字符中通用工具类
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-26		下午9:03:45 
 */
public class StringUtil {
	/**
	 * 将url列表转换为字符串表示，url间的分割符由delim定义
	 * @param  @param urlList
	 * @param  @param delim
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public static String convertUrlList(List<URL> urlList, String delim) {
		String us = "";
		for (URL url : urlList) {
			us += url.toString() + delim;
		}
		if (us.length() > 0)
			us = us.substring(0, us.length()-1);
		
		return us;
	}
}

