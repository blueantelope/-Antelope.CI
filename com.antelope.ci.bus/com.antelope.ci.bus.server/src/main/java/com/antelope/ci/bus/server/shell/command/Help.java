// com.antelope.ci.bus.server.shell.command.HelpContent.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import java.io.InputStream;
import java.util.List;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * 帮助信息
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-25		下午9:47:12 
 */
@XmlEntity(name="help")
public class Help {
	private List<HelpType> typeList;

	@XmlElement(name="type", isList=true, listClass=HelpType.class)
	public List<HelpType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<HelpType> typeList) {
		this.typeList = typeList;
	}
}

