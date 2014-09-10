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

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		下午8:03:52 
 */
@XmlEntity(name="contents")
public class Contents implements Serializable {
	private List<Content> contentList;
	
	public Contents() {
		super();
		contentList = new ArrayList<Content>();
	}
	
	@XmlElement(name="content", isList=true, listClass=Content.class)
	public List<Content> getContentList() {
		return contentList;
	}
	public void setContentList(List<Content> contentList) {
		this.contentList = contentList;
	}
	
	public List<List<String>> relist(int width) {
		List<List<String>> strList = new ArrayList<List<String>>();
		for (Content content : contentList)
			strList.addAll(content.relist(width));
		
		return strList;
	}
	
	public boolean isEmpty() {
		for (Content content : contentList) {
			if (!content.isEmpty())
				return false;
		}
		
		return true;
	}
}

