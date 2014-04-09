// com.antelope.ci.bus.server.shell.ShellText.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlCdata;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-31		下午5:50:07 
 */
@XmlEntity(name="text")
public class ShellText {
	protected String text;
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
	
	public static boolean isShellText(String str) {
		if (StringUtil.empty(str))
			return false;
		str = str.trim();
		if (StringUtil.startsWithIgnoreCase(str, "<text") && str.endsWith(">"))
			return true;
		return false;
	}
	
	@Override public String toString() {
		return toShellText(this);
	}
	
	public static int length(String str) {
		ShellText st = toShellText(str);
		return StringUtil.getWordCount(st.getText());
	}
	
	public static String toShellText(ShellText text) {
		StringBuffer buf = new StringBuffer();
		buf.append("<text font-size=\"").append(text.getFont_size()).append("\"").
		 		append(" font-style=\"").append(text.getFont_style()).append("\"").
		 		append(" font-mark=\"").append(text.getFont_mark()).append("\">").
		 		append(text.getText()).append("</text>");
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
		/*
		if (StringUtil.startsWithIgnoreCase(str, "<text") && str.endsWith(">")) {
			str = str.substring("<text".length(), str.length()-1);
			String[] ss = str.split(" ");
			for (String s : ss) {
				if (StringUtil.empty(s))
					continue;
				if (s.contains("=")) {
					String[] cs = s.split("=");
					if (cs.length == 2) {
						String k = cs[0];
						String v = cs[1];
						if (StringUtil.empty(k))
							continue;
						k = k.trim().toLowerCase();
						if (k.equals("font-size")) {
							try {
								int i = Integer.valueOf(v.trim());
								text.setFont_size(i);
							} catch (Exception e) {}
						} else if (k.equals("font-style")) {
							try {
								int i = Integer.valueOf(v.trim());
								text.setFont_style(i);
							} catch (Exception e) {}
						} else if (k.equals("font-mark")) {
							try {
								int i = Integer.valueOf(v.trim());
								text.setFont_mark(i);
							} catch (Exception e) {}
						}
					}
				}
			}
		}
		*/
		
		return text;
	}
}

