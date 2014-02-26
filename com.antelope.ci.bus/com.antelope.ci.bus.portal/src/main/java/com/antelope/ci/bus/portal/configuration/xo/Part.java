// com.antelope.ci.bus.portal.configuration.xo.Part.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		下午8:02:13 
 */
@XmlEntity(name="part")
public class Part {
	private String name;
	private Content content;
	private EU_Embed embed;
	private String embed_exp;
	private Integer sort;
	private List<Content> contentList;
	
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="content")
	public Content getContent() {
		if (content == null && contentList != null && !contentList.isEmpty()) 
			return contentList.get(0);
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	
	@XmlAttribute(name="embed")
	public String getEmbed_exp() {
		return embed_exp;
	}
	public void setEmbed_exp(String embed_exp) {
		this.embed_exp = embed_exp;
		try {
			this.embed = EU_Embed.toEmbed(this.embed_exp);
		} catch (CIBusException e) {
			e.printStackTrace();
		}
	}

	public EU_Embed getEmbed() {
		return embed;
	}
	
	@XmlAttribute(name="sort")
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public void addContet(Content content) {
		if (contentList == null) contentList = new ArrayList<Content>();
		contentList.add(content);
	}
	
	public void addContet(int index, Content content) {
		if (contentList == null) contentList = new ArrayList<Content>();
		int lLen = contentList.size();
		if (index > (lLen -1)) index = lLen;
		contentList.add(index, content);
	}
	
	public List<Content> getContentList() {
		return contentList;
	}
}

