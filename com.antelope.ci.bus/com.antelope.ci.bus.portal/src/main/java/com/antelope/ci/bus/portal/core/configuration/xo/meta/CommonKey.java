// com.antelope.ci.bus.portal.core.configuration.xo.meta.Key.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.meta;

import java.io.Serializable;

import com.antelope.ci.bus.common.xml.XmlCdata;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年9月28日		上午11:57:15 
 */
@XmlEntity(name="key")
public class CommonKey implements Serializable {
	protected String value;

	@XmlCdata public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}