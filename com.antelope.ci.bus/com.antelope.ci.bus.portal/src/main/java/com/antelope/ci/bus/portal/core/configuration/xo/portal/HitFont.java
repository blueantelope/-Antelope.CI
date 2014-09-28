// com.antelope.ci.bus.portal.core.configuration.xo.portal.HitFonr.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.Font;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年9月28日		上午11:23:00 
 */
@XmlEntity(name="font")
public class HitFont extends Font {
	public HitFont() {
		super();
	}

	public RenderFont toRenderFont() {
		return new RenderFont(style, size, mark);
	}
}

