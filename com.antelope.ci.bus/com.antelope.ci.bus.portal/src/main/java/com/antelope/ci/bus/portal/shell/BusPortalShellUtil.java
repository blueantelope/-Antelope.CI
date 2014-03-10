// com.antelope.ci.bus.portal.shell.BusShellUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.shell;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-10		下午2:36:45 
 */
public class BusPortalShellUtil {
	public static int max(int... array) throws CIBusException {
		if (array.length < 2)
			throw new CIBusException("", "at least two arguments");
		int max = 0;
		for (int t : array)
			max = max > t ? max : t;
		
		return max;
	}
}

