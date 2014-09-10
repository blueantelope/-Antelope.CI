// com.antelope.ci.bus.portal.core.configuration.xo.portal.Content.java
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
import java.util.Arrays;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_ContentType;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.FontExpression;
import com.antelope.ci.bus.server.shell.ShellText;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-9-10		上午11:45:41 
 */
@XmlEntity(name="contents")
public class Content implements Serializable {
	private String type;
	private List<ContentText> textList;
	private List<ContentBlocks> blocksList;

	
	public Content() {
		super();
		textList = new ArrayList<ContentText>();
		blocksList = new ArrayList<ContentBlocks>();
	}
	
	@XmlAttribute(name="type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public EU_ContentType getEUtype() throws CIBusException {
		return EU_ContentType.toType(type);
	}
	
	@XmlElement(name="value", isList=true, listClass=ContentText.class)
	public List<ContentText> getTextList() {
		return textList;
	}
	public void setTextList(List<ContentText> textList) {
		this.textList = textList;
	}

	@XmlElement(name="blocks", isList=true, listClass=ContentBlocks.class)
	public List<ContentBlocks> getBlocksList() {
		return blocksList;
	}
	public void setBlocksList(List<ContentBlocks> blocksList) {
		this.blocksList = blocksList;
	}
	
	public boolean isEmpty() {
		for (ContentText text : textList) {
			if (!StringUtil.empty(text.getValue()))
				return false;
		}
		for (ContentBlocks blocks : blocksList) {
			if (!blocks.isEmpty())
				return false;
		}
		
		return true;
	}
	
	public String getValue() {
		StringBuffer buf = new StringBuffer();
		for (ContentText text : textList) {
			if (!StringUtil.empty(text.getValue()))
				buf.append(text.getValue());
		}
		for (ContentBlocks blocks : blocksList) {
			if (!StringUtil.empty(blocks.getValue()))
				buf.append(blocks.getValue());
		}
		
		return buf.toString();
	}
	
	public List<String> getValueList() throws CIBusException {
		return Arrays.asList(StringUtil.toLines(getValue()));
	}
	
	public List<List<String>> relist(int width) {
		List<List<String>> strList = new ArrayList<List<String>>();
		strList.addAll(relistTextlist(width));
		strList.addAll(relistBlockslist(width));
		
		return strList;
	}
	
	public List<List<String>> relistBlockslist(int width) {
		List<List<String>> conList = new ArrayList<List<String>>();
		
		return conList;
	}
	
	public List<List<String>> relistTextlist(int width) {
		List<List<String>> conList = new ArrayList<List<String>>();
		if (!textList.isEmpty()) {
			try {
				addInnerList(conList, width);
			} catch (Exception e) {
				DevAssistant.assert_exception(e);
			}
		}
		
		return conList;
	}
	
	private void addInnerList(List<List<String>> conList,  int width) throws CIBusException {
		List<InnerText> valueList = new ArrayList<InnerText>();
		int index = 0;
		String totalValue = "";
		for (ContentText con : textList) {
			String sv = con.getValue();
			InnerText iv = new InnerText(index, index+sv.length(), con);
			valueList.add(iv);
			totalValue += sv;
			index += sv.length() + 1;
		}
		
		BufferedReader reader = new BufferedReader(new StringReader(totalValue));
		String line = null;
		int vl_index = 0;
		int vl_start = 0;
		int vl_position = 0;
		int vl_size = 0;
		List<String> innerList = new ArrayList<String>();
		try {
			while ((line = reader.readLine()) != null) {
				innerList = new ArrayList<String>();
				conList.add(innerList);
				
				vl_index = 0;
				vl_start = 0;
				vl_position = 0;
				vl_size = line.length();
				int iv_sum = 0;
				while (vl_index < vl_size) {
					InnerText innerText = valueList.get(vl_index);
					ContentText text = innerText.getText();
					String sv = text.getShellValue();
					int sv_len = StringUtil.getWordCount(sv);
					vl_start = vl_position + 1;
					iv_sum += sv_len;
					if (iv_sum <= width) {
						vl_position += sv_len;
						String inner_value = StringUtil.subString(line, vl_start, vl_position);
						addInner(innerList, text, inner_value);
					} else {
						vl_position += sv_len - (iv_sum - width) + 1;
						String inner_value = StringUtil.subString(line, vl_start, vl_position);
						addInner(innerList, text, inner_value);
						conList.add(innerList);
						innerList = new ArrayList<String>();
						vl_start = vl_position + 1;
						vl_position += iv_sum - width;
						inner_value = StringUtil.subString(line, vl_start, vl_position);
						addInner(innerList, text, inner_value);
						break;
					}
					
					vl_index++;
				}
			}
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
		
		if (!innerList.isEmpty()) 
			conList.add(innerList);
	}
	
	private void addInner(List<String> innerList, ContentText con, String inner_value) {
		ContentText inner_text = new ContentText();
		FontExpression font;
		if (con.isShellText()) {
			ShellText st = ShellText.toShellText(con.getShellValue());
			font = FontExpression.fromCode(st.getFont_mark(), st.getFont_size(), st.getFont_style());
		} else {
			font =con.getFont();
		}
		inner_text.setFont(font);
		inner_text.setValue(inner_value);
		innerList.add(inner_text.toString());
	}
	
	private static class InnerText {
		private int start;
		private int end;
		private ContentText text;
		public InnerText(int start, int end, ContentText text) {
			super();
			this.start = start;
			this.end = end;
			this.text = text;
		}
		public int getStart() {
			return start;
		}
		public int getEnd() {
			return end;
		}
		public ContentText getText() {
			return text;
		}
	}
}