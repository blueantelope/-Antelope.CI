// com.antelope.ci.bus.server.shell.ShellText.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import com.antelope.ci.bus.common.StringUtil;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-31		下午5:50:07 
 */
public class ShellText {
	protected String text;
	protected int font_size = 2;			// 1,small 2,medium 3,large
	protected int font_style = 1;			// 1,normal 2,bold 3, italic 
	protected int font_mark = 1;			// 1,normal, 2,line-through 3,shade
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getFont_size() {
		return font_size;
	}
	public void setFont_size(int font_size) {
		this.font_size = font_size;
	}
	public int getFont_style() {
		return font_style;
	}
	public void setFont_style(int font_style) {
		this.font_style = font_style;
	}
	public int getFont_mark() {
		return font_mark;
	}
	public void setFont_mark(int font_mark) {
		this.font_mark = font_mark;
	}
	
	public static ShellText toShellText(String str) {
		ShellText text = new ShellText();
		if (StringUtil.empty(str))
			return text;
		str = str.trim();
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
		
		return text;
	}
}

