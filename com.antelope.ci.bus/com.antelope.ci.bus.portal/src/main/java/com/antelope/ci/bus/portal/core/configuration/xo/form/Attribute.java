// com.antelope.ci.bus.portal.core.configuration.xo.form.Attribute.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.form;

import java.io.Serializable;

import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-5		上午11:37:02 
 */
@XmlEntity(name="attribute")
public class Attribute implements Serializable {
	public static final String FOCUS_DECOLLATOR = "->";
	
	private String focus;

	@XmlAttribute(name="focus")
	public String getFocus() {
		return focus;
	}
	public void setFocus(String focus) {
		this.focus = focus;
	}
}