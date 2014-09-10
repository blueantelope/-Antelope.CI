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

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
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
	
	public void addContent(Contents content) {
		initcontentsList();
		contentsList.add(content);
	}
	
	public void addContent(int index, Contents content) {
		initcontentsList();
		int lLen = contentsList.size();
		if (index > (lLen -1)) index = lLen;
		contentsList.add(index, content);
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
		for (Contents con : contentsList)
			strList.addAll(con.relist(width));
		
		return strList;
	} 
	
	// type 0: head 1: common -1: tail
	public void addContent(String desc, String div, Margin margin, EU_Position position, int type) {
		Contents add_content = new Contents();
		switch (position) {
			case START:
				addContent(add_content);
//				addBeforeContent(margin);
				break;
			case END:
//				addAfterContent(margin);
				addContent(add_content);
				break;
			case MIDDLE:
			default:
				switch (type) {
					case 0:		// head
						addContent(add_content);
//						addAfterContent(margin);
//						addContent(new Content(div));
						break;
					case -1:		// tail
//						addBeforeContent(margin);
						addContent(add_content);
						break;
					case 1:		// common
					default:
//						addBeforeContent(margin);
//						addContent(add_content);
//						addAfterContent(margin);
//						addContent(new Content(div));
						break;
					
				}
				break;
		}
	}
	
//	private void addBeforeContent(Margin margin) {
//		if (margin != null) {
//			Content bc = new Content(StringUtil.repeatString(" ", margin.getBefore()));
//			addContent(bc);
//		}
//	}
//	
//	private void addAfterContent(Margin margin) {
//		if (margin != null) {
//			Content ec = new Content(StringUtil.repeatString(" ", margin.getAfter()));
//			addContent(ec);
//		}
//	}
}

