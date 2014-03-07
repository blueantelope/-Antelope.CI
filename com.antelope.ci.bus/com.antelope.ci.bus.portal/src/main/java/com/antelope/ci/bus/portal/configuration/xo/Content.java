// com.antelope.ci.bus.portal.configuration.xo.Content.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import com.antelope.ci.bus.common.xml.XmlCdata;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		下午8:03:52 
 */
@XmlEntity(name="content")
public class Content {
	private String value;

	@XmlCdata
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

