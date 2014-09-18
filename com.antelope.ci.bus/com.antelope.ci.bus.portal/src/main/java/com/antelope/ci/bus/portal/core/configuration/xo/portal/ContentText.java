// com.antelope.ci.bus.portal.core.configuration.xo.portal.ContextText.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.XOUtil;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.CommonValue;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_BlockMode;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.FontExpression;
import com.antelope.ci.bus.server.shell.ShellText;


/**
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-9-4		下午5:32:07 
 */
@XmlEntity(name="value")
public class ContentText extends CommonValue {
	
	public ContentText() {
		super();
	}
	
	public ContentText(String value) {
		super(value);
	}
	
	public void relist(List<List<String>> strList, int width, boolean horizontal) throws CIBusException {
		try {
			BufferedReader reader = new BufferedReader(new StringReader(value));
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
				if (XOUtil.horizontalPoint(strList, checked, horizontal)) {
					lastList = strList.get(strList.size()-1);
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
					String textValue = genContentText(this, value).toString();
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
					addContentText(innerList, this, value);
					strList.add(innerList);
					start += StringUtil.lengthVT(value) + 1;
				}
			}
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
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
	
}