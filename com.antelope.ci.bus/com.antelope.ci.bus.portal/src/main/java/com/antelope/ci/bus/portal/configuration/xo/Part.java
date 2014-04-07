// com.antelope.ci.bus.portal.configuration.xo.Part.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.server.shell.ShellText;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		下午8:02:13 
 */
@XmlEntity(name="part")
public class Part implements Serializable {
	private String name;
	private Content content;
	private EU_Embed embed;
	private String embed_exp;
	private Integer sort;
	private List<Content> contentList;
	
	public Part() {
		super();
		contentList = new ArrayList<Content>();
	}
	
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
		contentList.add(content);
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
	
	public boolean contentEmpty() {
		if (contentList != null) {
			for (Content c : contentList)
				if (!StringUtil.empty(c.getValue()))
					return false;
		}
		
		return true;
	}
	
	public String getValue() {
		if (content == null || StringUtil.empty(content.getValue()))
			return "";
		return content.getValue();
	}
	
	public void addAfterValue(String s) {
		if (content == null || StringUtil.empty(content.getValue()))
			content = new Content();
		if (StringUtil.empty(content.getValue()))
			content.setValue("");
		content.setValue(content.getValue() + s);
	}
	
	public void addForeValue(String s) {
		if (content == null || StringUtil.empty(content.getValue()))
			content = new Content();
		if (StringUtil.empty(content.getValue()))
			content.setValue("");
		content.setValue(s + content.getValue());
	}
	
	public List<List<String>> reListContent(int width) {
		List<List<String>> conList = new ArrayList<List<String>>();
		List<String> innerList = new ArrayList<String>();
		int line_position = 0;
		if (contentList != null && !contentList.isEmpty()) {
			for (Content con : contentList) {
				try {
					addInnerContent(conList, innerList, con, line_position, width);
				} catch (Exception e) {
					DevAssistant.assert_exception(e);
				}
			}
			if (!innerList.isEmpty())
				conList.add(innerList);
		} else {
			try {
				addInnerContent(conList, innerList, content, line_position, width);
				if (!innerList.isEmpty())
					conList.add(innerList);
			} catch (Exception e) {
				DevAssistant.assert_exception(e);
			}
		}
		
		return conList;
	} 

	private void addInnerContent(List<List<String>> conList, List<String> innerList, 
			Content con, int line_position, int width) throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader(con.getShellValue()));
		boolean isShellText = con.isShellText();
		String line = null;
		int n = 0;
		while ((line = reader.readLine()) != null) {
			if (n > 0) {
				conList.add(innerList);
				innerList = new ArrayList<String>();
			}
			int con_count = StringUtil.getWordCount(line);
			int line_total = line_position + con_count;
			if (line_total > width) {
				int l_len = width - line_position;
				String up_value = StringUtil.subString(line, 0, l_len);
				String down_value = StringUtil.subString(line, l_len);
				if (isShellText) {
					up_value = ShellText.toShellText(line, up_value);
					down_value = ShellText.toShellText(line, down_value);
				}
				innerList.add(up_value);
				conList.add(innerList);
				innerList = new ArrayList<String>();
				innerList.add(down_value);
			} else {
				innerList.add(con.getValue());
				line_position += con_count;
			}
			n++;
		}
	}
	
	public String[] toLine(int width) throws IOException {
		if (contentList != null && !contentList.isEmpty()) {
			StringBuffer buf = new StringBuffer();
			for (Content con : contentList) 
				buf.append(con.getShellValue());
			return StringUtil.toLines(buf.toString());
		}
		
		return StringUtil.toLines(content.getShellValue());
	}
}

