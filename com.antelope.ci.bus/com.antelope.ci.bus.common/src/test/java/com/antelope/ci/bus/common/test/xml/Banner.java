// com.antelope.ci.bus.common.xml.test.Banner.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test.xml;

import com.antelope.ci.bus.common.xml.XmlCdata;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-15		下午6:05:12 
 */
@XmlEntity(name="banner")
public class Banner {
	private String text;

	@XmlCdata
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

