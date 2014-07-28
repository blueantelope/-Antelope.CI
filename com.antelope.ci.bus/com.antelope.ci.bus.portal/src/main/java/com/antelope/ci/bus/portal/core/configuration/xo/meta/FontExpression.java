// com.antelope.ci.bus.portal.core.configuration.xo.meta.FontRender.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.meta;

import java.io.Serializable;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-28		下午3:50:08 
 */
public class FontExpression implements Serializable {
	private EU_FontSize size;
	private EU_FontStyle sytle;
	private EU_FontMark mark;
	
	public EU_FontSize getSize() {
		return size;
	}
	public void setSize(EU_FontSize size) {
		this.size = size;
	}
	public EU_FontStyle getSytle() {
		return sytle;
	}
	public void setSytle(EU_FontStyle sytle) {
		this.sytle = sytle;
	}
	public EU_FontMark getMark() {
		return mark;
	}
	public void setMark(EU_FontMark mark) {
		this.mark = mark;
	}
	
	public static FontExpression fromRender(Font font) {
		FontExpression fontExp = new FontExpression();
		fontExp.setMark(font.toEU_Mark());
		fontExp.setSize(font.toEU_Size());
		fontExp.setSytle(font.toEU_Style());
		
		return fontExp;
	}
	
	public static FontExpression fromCode(int mark, int size, int style) {
		FontExpression fontExp = new FontExpression();
		fontExp.setMark(EU_FontMark.toMark(mark));
		fontExp.setSize(EU_FontSize.toSize(size));
		fontExp.setSytle(EU_FontStyle.toStyle(style));
		
		return fontExp;
	}
}

