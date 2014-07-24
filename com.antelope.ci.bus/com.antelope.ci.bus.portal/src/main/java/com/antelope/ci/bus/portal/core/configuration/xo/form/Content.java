// com.antelope.ci.bus.portal.core.configuration.xo.form.Content.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.form;

import java.io.Serializable;
import java.util.List;

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Place;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-11		下午2:47:51 
 */
@XmlEntity(name="content")
public class Content implements Serializable {
	private List<Group> groupList;

	@XmlElement(name="group", isList=true, listClass=Group.class)
	public List<Group> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}
}

