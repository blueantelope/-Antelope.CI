// com.antelope.ci.bus.server.shell.ShellText.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.base;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlCdata;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-31		下午5:50:07 
 */
@XmlEntity(name="text")
public class ShellText {
	public static final String TEXT_PREFIX = "<text";
	public static final String TEXT_SUFFIX= "</text>";
	public static final String P_PREFIX = "<p>";
	public static final String P_SUFFIX = "</p>";
	protected String text;
	protected int indent = 0; 
	protected int font_size = 2;			// 1,small 2,medium 3,large
	protected int font_style = 1;			// 1,normal 2,bold 3, italic 
	protected int font_mark = 1;			// 1,normal, 2,line-through 3,shade
	
	@XmlCdata
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@XmlAttribute(name="font-size")
	public int getFont_size() {
		return font_size;
	}
	public void setFont_size(int font_size) {
		this.font_size = font_size;
	}
	
	@XmlAttribute(name="font-style")
	public int getFont_style() {
		return font_style;
	}
	public void setFont_style(int font_style) {
		this.font_style = font_style;
	}
	
	@XmlAttribute(name="font-mark")
	public int getFont_mark() {
		return font_mark;
	}
	public void setFont_mark(int font_mark) {
		this.font_mark = font_mark;
	}
	
	@XmlAttribute(name="indent")
	public int getIndent() {
		return indent;
	}
	public void setIndent(int indent) {
		this.indent = indent;
	}
	
	public static boolean isShellText(String str) {
		return StringUtil.containEndsite(str, TEXT_PREFIX, TEXT_SUFFIX);
	}
	
	public static boolean containP(String str) {
		return StringUtil.containEndsite(str, P_PREFIX, P_SUFFIX);
	}
	
	@Override public String toString() {
		return toShellText(this);
	}
	
	public static int length(String str) {
		ShellText st = toShellText(str);
		return StringUtil.lengthVT(st.getText());
	}
	
	public static String toShellText(ShellText text) {
		return textToStr(text);
	}
	
	public static String toShellText(List<ShellText> textList) {
		StringBuffer buf = new StringBuffer();
		if (textList != null && !textList.isEmpty()) {
			buf.append(P_PREFIX);
			for (ShellText text : textList)
				buf.append(textToStr(text));
			buf.append(P_SUFFIX);
		}
		return buf.toString();
	}
	
	public static String toShellText(String str, String new_value) {
		ShellText text = toShellText(str);
		text.setText(new_value);
		return text.toString();
	}
	
	// example : <text font-size="1" font-style="1" font-mark="1">this is a testing</text>
	public static ShellText toShellText(String str) {
		ShellText text = new ShellText();
		if (StringUtil.empty(str))
			return text;
		str = str.trim();
		InputStream input = new ByteArrayInputStream(str.getBytes());
		try {
			text = (ShellText) BusXmlHelper.parse(ShellText.class, input);
		} catch (CIBusException e) {
			DevAssistant.assert_exception(e);
		}
		
		return text;
	}
	
	public static List<String> splitForP(String str) {
		List<String> splitList = new ArrayList<String>();
		splitForP(splitList, str, 0);
		
		return splitList;
	}
	
	public int placeholderWidth() {
		int width = StringUtil.lengthVT(text);
		if (indent > 0) width+= indent;
		return width;
	}
	
	private static void splitForP(List<String> splitList, String str, int from_index) {
		if (from_index > str.length())
			return;
		
		int start_index = str.indexOf(TEXT_PREFIX, from_index);
		int end_index = str.indexOf(TEXT_SUFFIX, from_index);
		if (start_index != -1 && end_index > start_index) {
			if (start_index > from_index) {
				strToSplitList(splitList, str, from_index, start_index);
			}
			splitList.add(str.substring(start_index, end_index+TEXT_SUFFIX.length()));
			from_index = end_index + TEXT_SUFFIX.length();
			splitForP(splitList, str, from_index);
		} else {
			strToSplitList(splitList, str, from_index, str.length());
		}
	}
	
	public static void strToSplitList(List<String> splitList, String str, int start, int end) {
		String s = str.substring(start, end);
		if (!s.startsWith(P_PREFIX) && !s.endsWith(P_SUFFIX))
			splitList.add(s);
	}
	
	private static String textToStr(ShellText text) {
		StringBuffer buf = new StringBuffer();
		buf.append("<text font-size=\"").append(text.getFont_size()).append("\"").
				append(" indent=\"").append(text.getIndent()).append("\"").
		 		append(" font-style=\"").append(text.getFont_style()).append("\"").
		 		append(" font-mark=\"").append(text.getFont_mark()).append("\">").
		 		append(text.getText()).append("</text>");
		return buf.toString();
	}
}

