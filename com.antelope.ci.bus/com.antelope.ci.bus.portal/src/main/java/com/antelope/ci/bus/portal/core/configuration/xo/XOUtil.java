// com.antelope.ci.bus.portal.core.configuration.xo.XOUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo;

import java.util.List;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-9-2		下午3:33:47 
 */
public class XOUtil {
	public static boolean on_off(String str) {
		if (str.trim().toLowerCase().equals("on"))
			return true;
		return false;
	}
	
	public static boolean horizontalPoint(List<List<String>> strList, boolean checked, boolean horizontal) {
		if (!checked 
				&& !strList.isEmpty() 
				&& !strList.get(strList.size()-1).isEmpty() 
				&& horizontal)
			return true;
		
		return false;
	}
}

