// com.antelope.ci.bus.server.shell.command.HelpTypeStatusContent.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlCdata;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		下午2:08:00 
 */
@XmlEntity(name="status")
public class HelpTypeStatus {
	private String name;
	private String content;

	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlCdata
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}

