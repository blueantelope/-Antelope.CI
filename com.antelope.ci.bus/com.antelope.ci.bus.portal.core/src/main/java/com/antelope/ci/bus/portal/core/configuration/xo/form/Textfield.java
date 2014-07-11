// com.antelope.ci.bus.portal.core.configuration.xo.form.Textfield.java
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
 * @Date	 2014-7-11		下午2:50:55 
 */
@XmlEntity(name="textfield")
public class Textfield implements Serializable {
	private Label label;
	private Field filed;
	
	@XmlElement(name="label")
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	
	@XmlElement(name="field")
	public Field getFiled() {
		return filed;
	}
	public void setFiled(Field filed) {
		this.filed = filed;
	}
}