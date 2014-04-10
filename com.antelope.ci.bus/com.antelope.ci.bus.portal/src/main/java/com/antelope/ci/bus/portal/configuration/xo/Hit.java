// com.antelope.ci.bus.portal.configuration.xo.Hist.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import java.io.Serializable;

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-10		上午11:14:11 
 */
@XmlEntity(name="hit")
public class Hit implements Serializable {
	protected RenderFont font;

	@XmlElement(name="font")
	public RenderFont getFont() {
		return font;
	}

	public void setFont(RenderFont font) {
		this.font = font;
	}
}

