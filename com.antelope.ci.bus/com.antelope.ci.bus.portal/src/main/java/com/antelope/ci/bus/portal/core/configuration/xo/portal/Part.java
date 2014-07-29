// com.antelope.ci.bus.portal.configuration.xo.Part.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.BufferedReader;
import java.io.IOException;
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
 * TODO 描述
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
		contentList = new Vector<Content>();
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
	
	private void initContentList(String div, Margin margin) {
		if (contentList == null) 
			contentList = new Vector<Content>();
		if (contentList.isEmpty()) {
			contentList.add(content);
			addAfterContent(margin);
			addContent(new Content(div));
		}
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
		if (contentList != null && contentList.size() > 1) {
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
		int size = contentList.size();
		List<String> innerList = new ArrayList<String>();
		List<InnerValue> valueList = new ArrayList<InnerValue>();
		int index = 0;
		String totalValue = "";
		for (Content con : contentList) {
			String sv = con.getShellValue();
			InnerValue iv = new InnerValue(index, index+sv.length(), con);
			valueList.add(iv);
			totalValue += sv;
		}
		
		BufferedReader reader = new BufferedReader(new StringReader(totalValue));
		String line = null;
		index = 0;
		int vl_index = 0;
		int vl_start = 0;
		int vl_position = 0;
		int n = 0;
		while ((line = reader.readLine()) != null) {
			if (n > 0) {
				conList.add(innerList);
				innerList = new ArrayList<String>();
			}
			
			int iv_sum = 0;
			while (vl_index < size) {
				InnerValue iv = valueList.get(vl_index);
				Content con = iv.getCon();
				String sv = con.getShellValue();
				int sv_len = StringUtil.getWordCount(sv);
				vl_start = vl_position + 1;
				iv_sum += sv_len;
				if (iv_sum <= width) {
					vl_position += sv_len;
					String inner_value = StringUtil.subString(totalValue, vl_start, vl_position);
					addInner(innerList, con, inner_value);
				} else {
					vl_position += sv_len - (iv_sum - width) + 1;
					String inner_value = StringUtil.subString(totalValue, vl_start, vl_position);
					addInner(innerList, con, inner_value);
					conList.add(innerList);
					innerList = new ArrayList<String>();
					vl_start = vl_position + 1;
					vl_position += iv_sum - width;
					inner_value = StringUtil.subString(totalValue, vl_start, vl_position);
					addInner(innerList, con, inner_value);
					break;
				}
				
				vl_index++;
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
	
	private static class InnerValue {
		private int start;
		private int end;
		private Content con;
		public InnerValue(int start, int end, Content con) {
			super();
			this.start = start;
			this.end = end;
			this.con = con;
		}
		public int getStart() {
			return start;
		}
		public int getEnd() {
			return end;
		}
		public Content getCon() {
			return con;
		}
	}

	private void addInnerContent(List<List<String>> conList, int width) throws Exception {
		List<String> innerList = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new StringReader(content.getShellValue()));
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
	
	public String[] toLine(int width) throws IOException {
		if (contentList != null && !contentList.isEmpty()) {
			StringBuffer buf = new StringBuffer();
			for (Content con : contentList) 
				buf.append(con.getShellValue());
			return StringUtil.toLines(buf.toString());
		}
		
		return StringUtil.toLines(content.getShellValue());
	}
	
	// type 0: head 1: common -1: tail
	public void addContent(String desc, String div, Margin margin, EU_Position position, int type) {
		Content add_content = new Content(desc);
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

