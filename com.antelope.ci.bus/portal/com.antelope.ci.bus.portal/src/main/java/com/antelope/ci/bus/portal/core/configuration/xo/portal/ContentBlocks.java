// com.antelope.ci.bus.portal.core.configuration.xo.portal.Blocks.java
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
import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.XOUtil;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.CommonValue;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_BlockMode;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.FontExpression;
import com.antelope.ci.bus.portal.core.shell.PortalShellText;
import com.antelope.ci.bus.server.shell.base.ShellText;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-9-2		下午3:08:50 
 */
@XmlEntity(name="blocks")
public class ContentBlocks implements Serializable {
	private String mode;
	private Render render;
	private List<ContentBlock> blockList;
	
	public ContentBlocks() {
		super();
		blockList = new ArrayList<ContentBlock>();
	}
	
	@XmlAttribute(name="mode")
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	@XmlElement(name="render")
	public Render getRender() {
		return render;
	}
	public void setRender(Render render) {
		this.render = render;
	}
	
	@XmlElement(name="block", isList=true, listClass=ContentBlock.class)
	public List<ContentBlock> getBlockList() {
		return blockList;
	}
	public void setBlockList(List<ContentBlock> blockList) {
		this.blockList = blockList;
	}
	
	public EU_BlockMode toBlockMode() {
		try {
			return EU_BlockMode.toMode(mode);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		return EU_BlockMode.HORIZONTAL;
	}
	
	public boolean isEmpty() {
		for (ContentBlock block : blockList) {
			if (block.active() && !StringUtil.empty(block.getValue()))
				return false;
		}
		
		return true;
	}
	
	public String getValue() {
		StringBuffer buf = new StringBuffer();
		for (ContentBlock block : blockList) {
			if (block.active() && !StringUtil.empty(block.getValue()))
				buf.append(block.getValue());
		}
		
		return buf.toString();
	}
	
	public String getShellValue() {
		String ret = "";
		switch (toBlockMode()) {
			case HORIZONTAL:
				int n = 0;
				for (ContentBlock block : blockList) {
					if (block.active() && !StringUtil.empty(block.getShellValue())) {
						if (n++ == 0)
							ret = block.getShellValue();
						else
							ret += "\n" + block.getShellValue();
					}
				}
				break;
			case VERTICAL:
				for (ContentBlock block : blockList) {
					if (block.active() && !StringUtil.empty(block.getShellValue())) {
						ret += block.getShellValue();
					}
				}
				break;
		}
		
		return ret;
	}
	
	public List<String> getShellValueList() throws CIBusException {
		return Arrays.asList(StringUtil.toLines(getShellValue()));
	}
	
	public void replace(BasicConfigrationReader[] readerList) {
		for (ContentBlock block : blockList)
			block.getCvalue().replaceValue(readerList);
	}
	
	public void relist(List<List<String>> strList, int width, boolean horizontal) throws CIBusException {
		boolean checked = false;
		for (ContentBlock block : blockList) {
			relistBlock(strList, width, block, checked, horizontal);
			checked = true;
		}
	}
	
	public void relistBlock(List<List<String>> strList, int width, ContentBlock block, boolean checked, boolean horizontal) throws CIBusException {
		try {
			BufferedReader reader = new BufferedReader(new StringReader(block.getValue()));
			int start = 0;
			int position = 0;
			int limit = 0;
			String line = null;
			List<String> innerList;
			List<String> lastList;
			String value;
			while ((line = reader.readLine()) != null) {
				start = 0;
				position = 0;
				limit = StringUtil.lengthVT(line);
				if (XOUtil.horizontalPoint(strList, checked, toBlockMode()==EU_BlockMode.HORIZONTAL||horizontal)) {
					lastList = strList.get(strList.size()-1);
					int lastindex = lastList.size() - 1;
					String last = lastList.get(lastindex);
					int lastsize;
					String last_temp = PortalShellText.peel(last);
					if (ShellText.isShellText(last))
						lastsize = ShellText.length(last_temp);
					else
						lastsize = StringUtil.lengthVT(last_temp);
					position = width - lastsize;
					if (position > limit)
						position = limit;
					value = StringUtil.subStringVT(line, start, position);
					lastList.add(genContentBlock(block, value).getShellValue());
					start = position + 1;
				}
				
				
				while (start < limit) {
					innerList = new ArrayList<String>();
					position += start + width;
					if (position > limit)
						position = limit;
					value = StringUtil.subStringVT(line, start, position);
					addContentBlock(innerList, block, value);
					strList.add(innerList);
					start += StringUtil.lengthVT(value) + 1;
				}
			}
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	private void addContentBlock(List<String> innerList, ContentBlock block, String value) {
		ContentBlock contentBlock = genContentBlock(block, value);
		innerList.add(contentBlock.getShellValue());
	}
	
	private ContentBlock genContentBlock(ContentBlock block, String value) {
		ContentBlock newContentBlock = new ContentBlock();
		newContentBlock.setName(block.getName());
		newContentBlock.setActive(block.getActive());
		newContentBlock.setAction(block.getAction());
		FontExpression font;
		CommonValue cValue = block.getCvalue();
		if (cValue.isShellText()) {
			ShellText st = ShellText.toShellText(cValue.getShellValue());
			font = FontExpression.fromCode(st.getFont_mark(), st.getFont_size(), st.getFont_style());
		} else {
			font = cValue.getFont();
		}
		CommonValue newCommonValue = cValue.clone();
		newCommonValue.setFont(font);
		newCommonValue.setValue(value);
		if (cValue.focus())
			newCommonValue.openFocus();
		newContentBlock.setCvalue(newCommonValue);
		
		return newContentBlock;
	}
	
	public void addBlockFont(RenderFont font) {
		for (ContentBlock block : blockList)
			block.getCvalue().setFont(font.toFontExpression());
	}
}

