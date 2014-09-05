// com.antelope.ci.bus.portal.configuration.xo.Part.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.BufferedReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Embed;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Position;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.FontExpression;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.Margin;
import com.antelope.ci.bus.server.shell.ShellText;


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
	private List<Content> contentList;
	private String value;
	
	public Part() {
		super();
		contentList = new Vector<Content>();
	}
	
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="content", isList=true, listClass=Place.class)
	public List<Content> getContentList() {
		return contentList;
	}
	public void setContentList(List<Content> contentList) {
		this.contentList = contentList;
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
	
	public void addContent(Content content) {
		initContentList();
		contentList.add(content);
	}
	
	public void addContent(int index, Content content) {
		initContentList();
		int lLen = contentList.size();
		if (index > (lLen -1)) index = lLen;
		contentList.add(index, content);
	}
	
	private void initContentList() {
		if (contentList == null) 
			contentList = new Vector<Content>();
	}
	
	public boolean contentEmpty() {
		for (Content content : contentList) {
			if (!content.isEmpty())
				return false;
		}
		
		return true;
	}
	
	public void initValue() {
		value = "";
		int n = 0;
		for (Content content : contentList) {
			if (!StringUtil.empty(content.getValue())) {
				if (n++ == 0)
					value = content.getValue();
				else
					value += "\n" + content.getValue();
			}
		}
	}
	
	public String getValue() {
		return value;
	}

	public List<List<String>> reListContent(int width) {
		List<List<String>> conList = new ArrayList<List<String>>();
		if (!contentList.isEmpty()) {
			try {
				addInnerList(conList, width);
			} catch (Exception e) {
				DevAssistant.assert_exception(e);
			}
		} else {
			try {
				addInnerContent(conList, width);
			} catch (Exception e) {
				DevAssistant.assert_exception(e);
			}
		}
		
		return conList;
	} 
	
	private void addInnerList(List<List<String>> conList,  int width) throws Exception {
		List<String> innerList = new ArrayList<String>();
		int n = 0;
		int i = 0;
		int start = 0;
		int position = 0;
		int limit = 0;
		for (Content con : contentList) {
			if (!con.isEmpty()) {
				if (n > 0) {
					conList.add(innerList);
					innerList = new ArrayList<String>();
				}
			}
			
			
			
			i = 1;
			start = 0;
			String v = con.getValue();
			limit = StringUtil.getWordCount(v);
			position = limit < width ? limit : width;
			while (start < limit) {
				String inner_value = StringUtil.subString(v, start, position);
				addInner(innerList, con, inner_value);
				
				if (limit < width)
				
				start += position;
				if (position < limit) {
				}
			}
			
			
			n++;
		}
		
		if (!innerList.isEmpty()) 
			conList.add(innerList);
	}
	
	private void addInner(List<String> innerList, Content con, String inner_value) {
		Content inner_con = new Content();
		FontExpression font;
		if (con.isShellText()) {
			ShellText st = ShellText.toShellText(con.getValue());
			font = FontExpression.fromCode(st.getFont_mark(), st.getFont_size(), st.getFont_style());
		} else {
			font =con.getFont();
		}
		inner_con.setFont(font);
		inner_con.setValue(inner_value);
		innerList.add(inner_con.toString());
	}
	
	private void addInnerContent(List<List<String>> conList, int width) throws Exception {
		List<String> innerList = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new StringReader(content.getValue()));
		boolean isShellText = content.isShellText();
		String line = null;
		int n = 0;
		while ((line = reader.readLine()) != null) {
			if (n > 0) {
				conList.add(innerList);
				innerList = new ArrayList<String>();
			}
			int line_count = StringUtil.getWordCount(line);
			if (line_count > width) {
				int m = 0;
				int position = 0;
				while (position < line_count) {
					int start = position;
					position  = (m + 1) * width;
					position = position < line_count ? position : line_count;
					int end = position;
					String line_value = StringUtil.subString(line, start, end);
					if (isShellText)
						line_value = ShellText.toShellText(line, line_value);
					innerList.add(line_value);
					conList.add(innerList);
					innerList = new ArrayList<String>();
					m++;
				}
			} else {
				innerList.add(line);
			}
			n++;
		}
		
		if (!innerList.isEmpty())
			conList.add(innerList);
	}
	
	public String[] toLine(int width) throws CIBusException {
		if (contentList != null && !contentList.isEmpty()) {
			StringBuffer buf = new StringBuffer();
			for (Content con : contentList) 
				buf.append(con.getValue());
			return StringUtil.toLines(buf.toString());
		}
		
		return StringUtil.toLines(content.getValue());
	}
	
	// type 0: head 1: common -1: tail
	public void addContent(String desc, String div, Margin margin, EU_Position position, int type) {
		Content add_content = new Content();
		switch (position) {
			case START:
				addContent(add_content);
				addBeforeContent(margin);
				break;
			case END:
				addAfterContent(margin);
				addContent(add_content);
				break;
			case MIDDLE:
			default:
				switch (type) {
					case 0:		// head
						addContent(add_content);
						addAfterContent(margin);
						addContent(new Content(div));
						break;
					case -1:		// tail
						addBeforeContent(margin);
						addContent(add_content);
						break;
					case 1:		// common
					default:
						addBeforeContent(margin);
						addContent(add_content);
						addAfterContent(margin);
						addContent(new Content(div));
						break;
					
				}
				break;
		}
	}
	
	private void addBeforeContent(Margin margin) {
		if (margin != null) {
			Content bc = new Content(StringUtil.repeatString(" ", margin.getBefore()));
			addContent(bc);
		}
	}
	
	private void addAfterContent(Margin margin) {
		if (margin != null) {
			Content ec = new Content(StringUtil.repeatString(" ", margin.getAfter()));
			addContent(ec);
		}
	}
}

