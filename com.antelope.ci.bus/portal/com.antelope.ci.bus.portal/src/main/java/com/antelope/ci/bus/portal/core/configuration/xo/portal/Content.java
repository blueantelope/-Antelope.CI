// com.antelope.ci.bus.portal.core.configuration.xo.portal.Content.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_BlockMode;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_ContentType;


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
		switch (toEUtype()) {
			case TEXT:
				for (ContentText text : textList) {
					if (!StringUtil.empty(text.getValue()))
						buf.append(text.getValue());
				}
				break;
			case BLOCK:
				for (ContentBlocks blocks : blocksList) {
					if (!StringUtil.empty(blocks.getValue()))
						buf.append(blocks.getValue());
				}
				break;
		}
		return buf.toString();
	}
	
	public List<String> getValueList() throws CIBusException {
		return Arrays.asList(StringUtil.toLines(getValue()));
	}
	
	public void replace(BasicConfigrationReader[] readerList) {
		switch (toEUtype()) {
			case TEXT:
				for (ContentText text : textList)
					text.replaceValue(readerList);
				break;
			case BLOCK:
				for (ContentBlocks blocks : blocksList)
					blocks.replace(readerList);
				break;
		}
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
		boolean first = true;
		EU_BlockMode mode = toBlockMode();
		for (ContentBlocks blocks : blocksList) {
			try {
				horizontal = first && (mode==EU_BlockMode.HORIZONTAL || horizontal);
				blocks.relist(strList, width, horizontal);
				first = false;
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	public void relistText(List<List<String>> strList, int width, boolean horizontal) {
		boolean first = true;
		EU_BlockMode mode = toBlockMode();
		for (ContentText text : textList) {
			try {
				horizontal = first && (mode==EU_BlockMode.HORIZONTAL || horizontal);
				text.relist(strList, width, horizontal);
				first = false;
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	public void addContentFont(RenderFont font) {
		switch (toEUtype()) {
			case TEXT:
				for (ContentText text : textList)
					text.setFont(font.toFontExpression());
				break;
			case BLOCK:
				for (ContentBlocks blocks : blocksList)
					blocks.addBlockFont(font);
				break;
		}
	}
}