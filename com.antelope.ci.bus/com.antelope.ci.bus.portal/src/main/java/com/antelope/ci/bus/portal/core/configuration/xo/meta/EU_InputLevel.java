// com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_InputLevel.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.meta;

import com.antelope.ci.bus.common.StringUtil;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-7		上午11:12:11 
 */
public enum EU_InputLevel {
	DEFAULT,
	OFF,
	COMMON;
	
	private static final String _DEFAULT = "_default";
	public static EU_InputLevel toInputLeve(String name) {
		if (StringUtil.empty(name) 
				|| "on".equalsIgnoreCase(name.trim()) 
				|| _DEFAULT.equalsIgnoreCase(name.trim())) {
			return EU_InputLevel.DEFAULT;
		} else {
			if ("off".equalsIgnoreCase(name.trim()))
				return EU_InputLevel.OFF;
			return EU_InputLevel.COMMON;
		}
	}
}

