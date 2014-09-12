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
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_BlockMode;
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
	private String mode;
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
	public EU_ContentType toEUtype() {
		try {
			return EU_ContentType.toType(type);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		
		return EU_ContentType.TEXT;
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
		return EU_BlockMode.HORIZONTAL;
	}
	
	@XmlElement(name="value", isList=true, listClass=ContentText.class)
	public List<ContentText> getTextList() {
		return textList;
	}
	public void setTextList(List<ContentText> textList) {
		this.textList = textList;
	}
	public void addText(ContentText text) {
		textList.add(text);
	}

	@XmlElement(name="blocks", isList=true, listClass=ContentBlocks.class)
	public List<ContentBlocks> getBlocksList() {
		return blocksList;
	}
	public void setBlocksList(List<ContentBlocks> blocksList) {
		this.blocksList = blocksList;
	}
	public void addBlocks(ContentBlocks blocks) {
		blocksList.add(blocks);
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
	
	public void relist(List<List<String>> strList, int width, boolean horizontal) {
		switch(toEUtype()) {
			case TEXT:
				relistText(strList, width, horizontal);
				break;
			case BLOCK:
				relistBlocks(strList, width, horizontal);
				break;
				
		}
	}
	
	public void relistBlocks(List<List<String>> strList, int width, boolean horizontal) {
		
	}
	
	public void relistText(List<List<String>> strList, int width, boolean horizontal) {
		boolean first = true;
		for (ContentText con : textList) {
			try {
				addContentText(strList, width, con, (horizontal && first));
				first = false;
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	private void addContentText(List<List<String>> conList, int width, ContentText text, boolean horizontal) throws CIBusException {
		try {
			BufferedReader reader = new BufferedReader(new StringReader(text.getValue()));
			int start = 0;
			int position = 0;
			int limit = 0;
			String line = null;
			List<String> innerList;
			List<String> lastList;
			boolean checked = false;
			String value;
			while ((line = reader.readLine()) != null) {
				start = 0;
				position = 0;
				limit = StringUtil.lengthVT(line);
				if (horizontalPoint(conList, checked, horizontal)) {
					lastList = conList.get(conList.size()-1);
					int lastindex = lastList.size() - 1;
					String last = lastList.get(lastindex);
					int lastsize;
					if (ShellText.isShellText(last))
						lastsize = ShellText.length(last);
					else
						lastsize = StringUtil.lengthVT(last);
					position = width - lastsize;
					if (position > limit)
						position = limit;
					value = StringUtil.subStringVT(line, start, position);
					String textValue = genContentText(text, value).toString();
					lastList.add(textValue);
					start = position + 1;
				}
				checked = true;
				
				while (start < limit) {
					innerList = new ArrayList<String>();
					position += start + width;
					if (position > limit)
						position = limit;
					value = StringUtil.subStringVT(line, start, position);
					addContentText(innerList, text, value);
					conList.add(innerList);
					start += StringUtil.lengthVT(value) + 1;
				}
			}
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	private boolean horizontalPoint(List<List<String>> conList, boolean checked, boolean horizontal) {
		if (!checked 
				&& !conList.isEmpty() 
				&& !conList.get(conList.size()-1).isEmpty() 
				&& (horizontal || toBlockMode() == EU_BlockMode.HORIZONTAL ))
			return true;
		
		return false;
	}
	
	private void addContentText(List<String> innerList, ContentText contentText, String value) {
		innerList.add(genContentText(contentText, value).toString());
	}
	
	private ContentText genContentText(ContentText contentText, String value) {
		ContentText newContentText = new ContentText();
		FontExpression font;
		if (contentText.isShellText()) {
			ShellText st = ShellText.toShellText(contentText.getShellValue());
			font = FontExpression.fromCode(st.getFont_mark(), st.getFont_size(), st.getFont_style());
		} else {
			font =contentText.getFont();
		}
		newContentText.setFont(font);
		newContentText.setValue(value);
		
		return newContentText;
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