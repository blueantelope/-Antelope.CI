// com.antelope.ci.bus.portal.core.configuration.xo.portal.HitGroup.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年9月28日		上午10:47:15 
 */
@XmlEntity(name="hitgroup")
public class HitGroup extends CommonHit {
	private Key key;
	
	@XmlElement(name="key")
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}

	public boolean isBlock() {
		if (name.trim().equalsIgnoreCase("block"))
			return true;
		return false;
	}
}