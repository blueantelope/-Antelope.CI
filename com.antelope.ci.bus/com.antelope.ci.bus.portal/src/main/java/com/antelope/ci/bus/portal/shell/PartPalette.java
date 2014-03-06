// com.antelope.ci.bus.portal.shell.PartPalette.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.shell;


/**
 * it is rectangle
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-6		下午3:27:03 
 */
public class PartPalette {
	private int x;
	private int y;
	private int width;
	private int height;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setStartPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setShapePoint(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
}

