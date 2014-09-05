// com.antelope.ci.bus.portal.configuration.xo.Content.java
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

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		下午8:03:52 
 */
@XmlEntity(name="content")
public class Content implements Serializable {
	private List<ContentText> textList;
	private BlockGroup blockGroup;
	
	public Content() {
		super();
		textList = new ArrayList<ContentText>();
	}


	public List<ContentText> getTextList() {
		return textList;
	}
	public void setTextList(List<ContentText> textList) {
		this.textList = textList;
	}


	@XmlElement(name="blocks")
	public BlockGroup getBlockGroup() {
		return blockGroup;
	}
	public void setBlockGroup(BlockGroup blockGroup) {
		this.blockGroup = blockGroup;
	}
	
	public boolean isEmpty() {
		if (!textList.isEmpty()) {
			for (ContentText text : textList) {
				if (!StringUtil.empty(text.getValue()))
					return false;
			}
		} else if (null != blockGroup) {
			if (!blockGroup.isEmpty())
				return false;
		}
		
		return true;
	}
	
	public String getValue() {
		String ret = "";
		if (!textList.isEmpty()) {
			int n = 0;
			for (ContentText text : textList) {
				if (!StringUtil.empty(text.getValue())) {
					if (n++ == 0)
						ret = text.getShellValue();
					else
						ret += "\n" + text.getShellValue();
				}
			}
		} else if (null != blockGroup) {
			ret = blockGroup.getShellValue();
		}
		
		return ret;
	}
	
	public List<String> getValueList() throws CIBusException {
		return Arrays.asList(StringUtil.toLines(getValue()));
	}
}

