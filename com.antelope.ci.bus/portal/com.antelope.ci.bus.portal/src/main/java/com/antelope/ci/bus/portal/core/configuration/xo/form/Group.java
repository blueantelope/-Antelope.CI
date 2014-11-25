// com.antelope.ci.bus.portal.core.configuration.xo.form.Group.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-11		下午2:49:13 
 */
@XmlEntity(name="group")
public class Group implements Serializable {
	private List<Component> componentList = new ArrayList<Component>();

	@XmlElement(name="component", isList=true, listClass=Component.class)
	public List<Component> getComponentList() {
		return componentList;
	}
	public void setComponentList(List<Component> componentList) {
		this.componentList = componentList;
	}
}

