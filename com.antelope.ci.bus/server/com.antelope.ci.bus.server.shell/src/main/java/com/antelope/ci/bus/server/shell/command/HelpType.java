// com.antelope.ci.bus.server.shell.HelpTypeContent.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import java.util.List;

import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		下午2:05:54 
 */
@XmlEntity(name="type")
public class HelpType {
	private String name;
	private List<HelpTypeStatus> statusList;

	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="status", isList=true,  listClass=HelpTypeStatus.class)
	public List<HelpTypeStatus> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<HelpTypeStatus> statusList) {
		this.statusList = statusList;
	}
}

