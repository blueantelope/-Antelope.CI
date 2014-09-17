// com.antelope.ci.bus.portal.configuration.xo.Content.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_BlockMode;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		下午8:03:52 
 */
@XmlEntity(name="contents")
public class Contents implements Serializable {
	private String mode;
	private List<Content> contentList;
	
	public Contents() {
		super();
		contentList = new ArrayList<Content>();
	}
	
	@XmlAttribute(name="mode")
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public EU_BlockMode toBlockMode() {
		try {
			return EU_BlockMode.toMode(mode);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		return EU_BlockMode.VERTICAL;
	}
	
	@XmlElement(name="content", isList=true, listClass=Content.class)
	public List<Content> getContentList() {
		return contentList;
	}
	public void setContentList(List<Content> contentList) {
		this.contentList = contentList;
	}
	
	public void addContent(Content content) {
		contentList.add(content);
	}
	
	public void relist(List<List<String>> strList, int width, boolean horizontal) {
		boolean first = true;
		for (Content content : contentList) {
			if (first && horizontal)
				content.relist(strList, width, true);
			else if (!first && toBlockMode()==EU_BlockMode.HORIZONTAL)
				content.relist(strList, width, true);
			else
				content.relist(strList, width, false);
			first = false;
		}
	}
	
	public boolean isEmpty() {
		for (Content content : contentList) {
			if (!content.isEmpty())
				return false;
		}
		
		return true;
	}
	
	public void replace(BasicConfigrationReader[] readerList) {
		for (Content content : contentList)
			content.replace(readerList);
	}
}

