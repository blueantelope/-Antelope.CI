// com.antelope.ci.bus.portal.core.shell.PortalBlock.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell;

import com.antelope.ci.bus.server.shell.base.ShellMode.BaseShellModeType;
import com.antelope.ci.bus.server.shell.buffer.ShellCursor;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-26		上午10:24:22 
 */
public class PortalBlock {
	private String name;
	private ShellCursor cursor;
	private int width;
	private int height;
	private String mode;
	private String value;
	private PortalBlock up;
	private PortalBlock down;
	private PortalBlock left;
	private PortalBlock right;
	private boolean _markUp;
	private boolean _markDown;
	private boolean  _available;
	
	public PortalBlock() {
		this(BaseShellModeType.MAIN.getName());
	}
	
	public PortalBlock(String mode) {
		up = null;
		down = null;
		left = null;
		right = null;
		this.mode = mode;
		this._markUp = false;
		this._markDown = false;
		this._available = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ShellCursor getCursor() {
		return cursor;
	}
	public void setCursor(ShellCursor cursor) {
		this.cursor = cursor;
	}

	public String getMode() {
		return mode;
	}
	
	public PortalBlock getUp() {
		return up;
	}
	public void setUp(PortalBlock up) {
		this.up = up;
	}

	public PortalBlock getDown() {
		return down;
	}
	public void setDown(PortalBlock down) {
		this.down = down;
	}

	public PortalBlock getLeft() {
		return left;
	}
	public void setLeft(PortalBlock left) {
		this.left = left;
	}

	public PortalBlock getRight() {
		return right;
	}
	public void setRight(PortalBlock right) {
		this.right = right;
	}
	
	public void markUp() {
		this._markUp = true;
	}
	public boolean isMarkUp() {
		return _markUp;
	}
	
	public void markDown() {
		this._markDown = true;
	}
	public boolean isMarkDown() {
		return _markDown;
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

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean available() {
		return _available;
	}
	public void disable() {
		this._available = false;
	}
	public void enable() {
		this._available = true;
	}
}