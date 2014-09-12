// com.antelope.ci.bus.portal.configuration.xo.Part.java
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
import java.util.Vector;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_BlockMode;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_ContentType;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Embed;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Position;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.Margin;


/**
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		下午8:02:13 
 */
@XmlEntity(name="part")
public class Part implements Serializable {
	private String name;
	private EU_Embed embed;
	private String embed_exp;
	private Integer sort;
	private String mode;
	private List<Contents> contentsList;
	private String value;
	
	public Part() {
		super();
		contentsList = new ArrayList<Contents>();
	}
	
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="contents", isList=true, listClass=Contents.class)
	public List<Contents> getContentsList() {
		return contentsList;
	}
	public void setContentsList(List<Contents> contentsList) {
		this.contentsList = contentsList;
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
	
	private void initcontentsList() {
		if (contentsList == null) 
			contentsList = new Vector<Contents>();
	}
	
	public boolean contentEmpty() {
		for (Contents content : contentsList) {
			if (!content.isEmpty())
				return false;
		}
		
		return true;
	}
	
	public String getValue() {
		return value;
	}

	public List<List<String>> relist(int width) {
		List<List<String>> strList = new ArrayList<List<String>>();
		for (Contents contents : contentsList)
			contents.relist(strList, width, toBlockMode()==EU_BlockMode.HORIZONTAL);
		
		return strList;
	}
	
	public void addContents(String value, String div, Margin margin, EU_Position position, int type) {
		Contents newContents = createTextContent(value);
		addContents(newContents, div, margin, position, type);
	}
	
	// type 0: head 1: common -1: tail
	public void addContents(Contents newContents, String div, Margin margin, EU_Position position, int type) {
		switch (position) {
			case START:
				contentsList.add(newContents);
				addBeforeTextContent(margin);
				break;
			case END:
				addAfterTextContent(margin);
				contentsList.add(newContents);
				break;
			case MIDDLE:
			default:
				switch (type) {
					case 0:		// head
						contentsList.add(newContents);
						addAfterTextContent(margin);
						addTextContent(div);
						break;
					case -1:		// tail
						addBeforeTextContent(margin);
						contentsList.add(newContents);
						break;
					case 1:		// common
					default:
						addBeforeTextContent(margin);
						contentsList.add(newContents);
						addAfterTextContent(margin);
						addTextContent(div);
						break;
					
				}
				break;
		}
	}
	
	private void addBeforeTextContent(Margin margin) {
		if (margin != null)
			addTextContent(" ", margin.getBefore());
	}
	
	private void addAfterTextContent(Margin margin) {
		if (margin != null)
			addTextContent(" ", margin.getAfter());
	}
	
	private void addTextContent(String repeat, int times) {
		addTextContent(StringUtil.repeatString(repeat, times));
	}
	
	private void addTextContent(String value) {
		Contents contents =createTextContent(value);
		contentsList.add(contents);
	}
	
	private Contents createTextContent(String value) {
		ContentText text = new ContentText(value);
		
		Content content = new Content();
		content.setMode(EU_BlockMode.HORIZONTAL.getName());
		content.setType(EU_ContentType.TEXT.getName());
		content.addText(text);
		
		Contents contents = new Contents();
		contents.setMode(EU_BlockMode.HORIZONTAL.getName());
		contents.addContent(content);
		
		return contents;
	}
}

