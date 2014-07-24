// com.antelope.ci.bus.portal.configuration.xo.ContentFont.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.Serializable;

import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_FontMark;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_FontSize;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_FontStyle;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-4		下午6:27:18 
 */
public class ContentFont implements Serializable {
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
	
	public static ContentFont fromRender(RenderFont render) {
		ContentFont font = new ContentFont();
		font.setMark(render.toEU_Mark());
		font.setSize(render.toEU_Size());
		font.setSytle(render.toEU_Style());
		
		return font;
	}
	
	public static ContentFont fromCode(int mark, int size, int style) {
		ContentFont font = new ContentFont();
		font.setMark(EU_FontMark.toMark(mark));
		font.setSize(EU_FontSize.toSize(size));
		font.setSytle(EU_FontStyle.toStyle(style));
		
		return font;
	}
}

