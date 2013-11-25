// com.antelope.ci.bus.server.shell.command.HelpContent.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import com.antelope.ci.bus.common.xml.XmlCdata;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-25		下午9:47:12 
 */
@XmlEntity(name="help")
public class HelpContent {
	private String content;

	@XmlCdata
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}

