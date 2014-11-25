// com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Widget.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.meta;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-7		上午11:57:51 
 */
public enum EU_Widget {
	LABEL("label"),
	FIELD("field");
	
	private String name;
	private EU_Widget(String name) {
		this.name= name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	public static EU_Widget fromName(String str) throws CIBusException {
		for (EU_Widget widget : EU_Widget.values()) {
			if (widget.getName().equalsIgnoreCase(str))
				return widget;
		}
		
		throw new CIBusException("unknown widget");
	}
	
}

