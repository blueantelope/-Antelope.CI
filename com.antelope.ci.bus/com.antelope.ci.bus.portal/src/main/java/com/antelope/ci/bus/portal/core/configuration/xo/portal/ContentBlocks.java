// com.antelope.ci.bus.portal.core.configuration.xo.portal.Blocks.java
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
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_BlockMode;


/**
 * TODO 描述
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
}

