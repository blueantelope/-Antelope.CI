// com.antelope.ci.bus.portal.core.shell.BusPortalInputBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.buffer;

import java.util.List;

import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.BusPortalShellLiving;
import com.antelope.ci.bus.portal.core.shell.BusPortalShellLiving.BusPortalShellUnit;
import com.antelope.ci.bus.server.shell.buffer.BusInputBuffer;
import com.antelope.ci.bus.server.shell.buffer.ShellArea.DIRECTION;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年10月10日		下午4:20:59 
 */
public class BusPortalInputBuffer extends BusInputBuffer {
	public enum NEXTBUFFER{KEEP, UP, DOWN, LEFT, RIGHT};
	protected String name;
	protected BusPortalShell shell;
	protected NEXTBUFFER nextbuffer;
	protected BusPortalInputBuffer up;
	protected BusPortalInputBuffer down;
	protected BusPortalInputBuffer left;
	protected BusPortalInputBuffer right;
	
	
	public BusPortalInputBuffer(BusPortalShell shell, int x, int y, int width, int height, String name) {
		super(shell.getIO(), x, y, width, height);
		this.name = name;
		this.shell = shell;
		nextbuffer = NEXTBUFFER.KEEP;
		up = null;
		down = null;
		left = null;
		right = null;
	}
	
	public String getName() {
		return name;
	}
	
	public BusPortalInputBuffer getUp() {
		return up;
	}
	public void setUp(BusPortalInputBuffer up) {
		this.up = up;
	}

	public BusPortalInputBuffer getDown() {
		return down;
	}
	public void setDown(BusPortalInputBuffer down) {
		this.down = down;
	}

	public BusPortalInputBuffer getLeft() {
		return left;
	}
	public void setLeft(BusPortalInputBuffer left) {
		this.left = left;
	}

	public BusPortalInputBuffer getRight() {
		return right;
	}
	public void setRight(BusPortalInputBuffer right) {
		this.right = right;
	}
	
	public boolean next() {
		return nextbuffer != NEXTBUFFER.KEEP;
	}
	
	public BusPortalInputBuffer nextBuffer() {
		switch (nextbuffer) {
			case UP:
				return up;
			case DOWN:
				return down;
			case LEFT:
				return left;
			case RIGHT:
				return right;
			default:
				return null;
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusAreaBuffer#rewriteAhead(int, int)
	 */
	@Override
	protected void rewriteAhead(int x, int y) {
		shell.commomIO();
		BusPortalShellLiving shellLiving = shell.getLiving();
		List<BusPortalShellUnit> units = shellLiving.getAheadLine(x, y);
		shell.rewriteUnits(units);
		shell.editIO();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusAreaBuffer#rewriteLatter(int, int)
	 */
	@Override
	protected void rewriteLatter(int x, int y) {
		shell.commomIO();
		BusPortalShellLiving shellLiving = shell.getLiving();
		List<BusPortalShellUnit> units = shellLiving.getLatterLine(x, y);
		shell.rewriteUnits(units);
		shell.editIO();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusAreaBuffer#userDown(com.antelope.ci.bus.server.shell.buffer.ShellArea.DIRECTION)
	 */
	@Override
	protected void userDown(DIRECTION direction) {
		if (direction == DIRECTION.OUTSIDE)
			nextbuffer = NEXTBUFFER.DOWN;
	}
}