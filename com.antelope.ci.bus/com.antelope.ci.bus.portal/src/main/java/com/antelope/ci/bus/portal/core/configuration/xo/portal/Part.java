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
	private List<Contents> allContentsList;
	private String value;
	
	public Part() {
		super();
		contentsList = new ArrayList<Contents>();
		allContentsList = new ArrayList<Contents>();
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
	
	public List<Contents> getAllContentsList() {
		return allContentsList;
	}
	public void setAllContentsList(List<Contents> allContentsList) {
		this.allContentsList = allContentsList;
	}
	public void addContentsList(List<Contents> newContentsList, EU_Position position) {
		switch (position) {
			case START:
				allContentsList.addAll(0, newContentsList);
				break;
			default:
				allContentsList.addAll(newContentsList);
				break;
		}
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
		List<Contents> reContentslist = allContentsList;
		if (allContentsList.isEmpty())
			reContentslist = contentsList;
		for (Contents contents : reContentslist)
			contents.relist(strList, width, toBlockMode()==EU_BlockMode.HORIZONTAL);
		
		return strList;
	}
	
	public static List<Contents> createContensList(Part part, String str, String div, Margin margin, EU_Position position) {
		Contents newContents = createTextContent(str);
		return createContensList(part, newContents, div, margin, position);
	}
	
	public static List<Contents> createContensList(String str, String div, Margin margin, EU_Position position) {
		Contents newContents = createTextContent(str);
		return createContensList(newContents, div, margin, position);
	}
	
	private static List<Contents> createContensList(Part part, Contents newContents, String div, Margin margin, EU_Position position) {
		List<Contents> newContentsList = new ArrayList<Contents>();
		switch (position) {
			case START:
				addNewContents(newContentsList, part.getContentsList());
				addNewContents(newContentsList, newContents);
				addAfterTextContent(newContentsList, margin);
				addTextContent(newContentsList, div);
				break;
			case END:
				addBeforeTextContent(newContentsList, margin);
				addNewContents(newContentsList, newContents);
				addNewContents(newContentsList, part.getContentsList());
				break;
			case MIDDLE:
			default:
				addBeforeTextContent(newContentsList, margin);
				addNewContents(newContentsList, newContents);
				addNewContents(newContentsList, part.getContentsList());
				addAfterTextContent(newContentsList, margin);
				addTextContent(newContentsList, div);
				break;
		}
		
		return newContentsList;
	}
	
	private static List<Contents> createContensList(Contents newContents, String div, Margin margin, EU_Position position) {
		List<Contents> newContentsList = new ArrayList<Contents>();
		switch (position) {
			case START:
				addNewContents(newContentsList, newContents);
				addAfterTextContent(newContentsList, margin);
				addTextContent(newContentsList, div);
				break;
			case END:
				addBeforeTextContent(newContentsList, margin);
				addNewContents(newContentsList, newContents);
				break;
			case MIDDLE:
			default:
				addBeforeTextContent(newContentsList, margin);
				addNewContents(newContentsList, newContents);
				addAfterTextContent(newContentsList, margin);
				addTextContent(newContentsList, div);
				break;
		}
		
		return newContentsList;
	}
	
	public static List<Contents> createStartContentsList(String str, Margin margin) {
		List<Contents> newContentsList = new ArrayList<Contents>();
		Contents newContents = createTextContent(str);
		addNewContents(newContentsList, newContents);
		addBeforeTextContent(newContentsList, margin);
		return newContentsList;
	}
	
	public static List<Contents> createEndContentsList(String str, Margin margin) {
		List<Contents> newContentsList = new ArrayList<Contents>();
		Contents newContents = createTextContent(str);
		addAfterTextContent(newContentsList, margin);
		addNewContents(newContentsList, newContents);
		return newContentsList;
	}
	
	public void startContents(String str, Margin margin) {
		Contents newContents = createTextContent(str);
		addNewContents(contentsList, newContents);
		addBeforeTextContent(contentsList, margin);
	}
	
	public void endContents(String str, Margin margin) {
		Contents newContents = createTextContent(str);
		addAfterTextContent(contentsList, margin);
		addNewContents(contentsList, newContents);
	}
	
	private static void addBeforeTextContent(List<Contents> newContentsList, Margin margin) {
		if (margin != null)
			addTextContent(newContentsList, " ", margin.getBefore());
	}
	
	private static void addAfterTextContent(List<Contents> newContentsList, Margin margin) {
		if (margin != null)
			addTextContent(newContentsList, " ", margin.getAfter());
	}
	
	private static void addTextContent(List<Contents> newContentsList, String repeat, int times) {
		addTextContent(newContentsList, StringUtil.repeatString(repeat, times));
	}
	
	private static void addTextContent(List<Contents> newContentsList, String value) {
		Contents contents = createTextContent(value);
		addNewContents(newContentsList, contents);
	}
	
	private static void addNewContents(List<Contents> newContentsList, List<Contents> addContentsList)  {
		if (!addContentsList.isEmpty())
			newContentsList.addAll(addContentsList);
	}
	
	private static void addNewContents(List<Contents> newContentsList, Contents newContents)  {
		if (null != newContents)
			newContentsList.add(newContents);
	}
	
	private static Contents createTextContent(String str) {
		if (StringUtil.empty(str))
			return null;
		
		ContentText text = new ContentText(str);
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

