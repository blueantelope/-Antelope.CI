// com.antelope.ci.bus.portal.core.configuration.xo.form.Box.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.form;

import java.io.Serializable;

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-6		下午6:20:52 
 */
@XmlEntity(name="box")
public class Box implements Serializable {
	private BoxUp up;
	private BoxBottom bottom;
	private BoxLeft left;
	private BoxRight right;
	
	@XmlElement(name="up")
	public BoxUp getUp() {
		return up;
	}
	public void setUp(BoxUp up) {
		this.up = up;
	}
	
	@XmlElement(name="bottom")
	public BoxBottom getBottom() {
		return bottom;
	}
	public void setBottom(BoxBottom bottom) {
		this.bottom = bottom;
	}
	
	@XmlElement(name="left")
	public BoxLeft getLeft() {
		return left;
	}
	public void setLeft(BoxLeft left) {
		this.left = left;
	}
	
	@XmlElement(name="right")
	public BoxRight getRight() {
		return right;
	}
	public void setRight(BoxRight right) {
		this.right = right;
	}
}

