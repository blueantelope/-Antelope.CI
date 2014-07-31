// com.antelope.ci.bus.portal.core.configuration.xo.form.Widget.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.form;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.server.shell.ShellText;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-11		下午2:57:53 
 */
public class Widget implements Serializable {
	protected String length;
	protected String row_size;
	protected String column_width;
	protected Style style;
	
	@XmlAttribute(name="length")
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	
	@XmlAttribute(name="row_size")
	public String getRow_size() {
		return row_size;
	}
	public void setRow_size(String row_size) {
		this.row_size = row_size;
	}
	
	@XmlAttribute(name="column_width")
	public String getColumn_width() {
		return column_width;
	}
	public void setColumn_width(String column_width) {
		this.column_width = column_width;
	}
	
	@XmlElement(name="style")
	public Style getStyle() {
		return style;
	}
	public void setStyle(Style style) {
		this.style = style;
	}
	
	public int percentForWidth() throws CIBusException {
		if (column_width.trim().endsWith("%")) {
			NumberFormat nformat = NumberFormat.getPercentInstance();
			try {
				return nformat.parse(column_width.trim()).intValue();
			} catch (ParseException e) {
				throw new CIBusException("", e);
			}
		}
		
		return Integer.parseInt(column_width.trim());
	}
	
	public int getComponetWidth(int width) throws CIBusException {
		float widthrate = percentForWidth() / 100f;
		int componet_width = (int) (widthrate * width);
		int len = Integer.parseInt(length);
		return componet_width > len ? len : componet_width;
	}
	
	public int getRowSize() {
		return Integer.parseInt(row_size);
	}
	
	public ShellText toShellText(String str) {
		return Style.genShellText(style,  str);
	}
}

