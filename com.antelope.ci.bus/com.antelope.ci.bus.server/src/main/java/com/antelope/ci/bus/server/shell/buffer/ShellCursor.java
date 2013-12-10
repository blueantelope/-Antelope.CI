// com.antelope.ci.bus.server.shell.buffer.ShellCursor.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.buffer;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-10		上午11:24:12 
 */
public class ShellCursor {
	private int x;
	private int y;

	public ShellCursor(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void left(int n) {
		x -= n;
	}
	
	public void right(int n) {
		x += n;
	}
	
	public void up(int n) {
		y -= n;
	}
	
	public void down(int n) {
		y += n;
	}
	
	public void newLine() {
		x = 0;
		y += 1;
	}
	
	public void lastEndline(int width) {
		x = width;
		y -= 0;
	}
}

