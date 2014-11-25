// com.antelope.ci.bus.portal.core.shell.BusPortalInputBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.buffer;

import java.io.IOException;
import java.util.List;

import com.antelope.ci.bus.common.exception.CIBusException;
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
	public enum NEIGHBOR{KEEP, UP, DOWN, LEFT, RIGHT};
	protected String name;
	protected BusPortalShell shell;
	protected NEIGHBOR neighbor;
	protected boolean markUp;
	protected BusPortalInputBuffer up;
	protected boolean markDown;
	protected BusPortalInputBuffer down;
	protected BusPortalInputBuffer left;
	protected boolean markLeft;
	protected BusPortalInputBuffer right;
	protected boolean markRight;
	
	
	public BusPortalInputBuffer(BusPortalShell shell, int x, int y, int width, int height, String name) {
		super(shell.getIO(), x, y, width, height);
		this.name = name;
		this.shell = shell;
		neighbor = NEIGHBOR.KEEP;
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
		this.markUp = true;
		this.up = up;
	}

	public BusPortalInputBuffer getDown() {
		return down;
	}
	public void setDown(BusPortalInputBuffer down) {
		this.markDown = true;
		this.down = down;
	}

	public BusPortalInputBuffer getLeft() {
		return left;
	}
	public void setLeft(BusPortalInputBuffer left) {
		this.markLeft = true;
		this.left = left;
	}

	public BusPortalInputBuffer getRight() {
		return right;
	}
	public void setRight(BusPortalInputBuffer right) {
		this.markRight = true;
		this.right = right;
	}
	
	public boolean isMarkUp() {
		return markUp;
	}
	public boolean isMarkDown() {
		return markDown;
	}
	public boolean isMarkLeft() {
		return markLeft;
	}
	public boolean isMarkRight() {
		return markRight;
	}
	public boolean fullNeighbors() {
		return markUp && markDown && markLeft && markRight;
	}
	
	public boolean goNeighbor() {
		return neighbor != NEIGHBOR.KEEP;
	}
	
	public BusPortalInputBuffer nextBuffer() {
		switch (neighbor) {
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
			neighbor = NEIGHBOR.DOWN;
		else
			neighbor =  NEIGHBOR.KEEP;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusAreaBuffer#userUp(com.antelope.ci.bus.server.shell.buffer.ShellArea.DIRECTION)
	 */
	@Override
	protected void userUp(DIRECTION direction) {
		if (direction == DIRECTION.OUTSIDE)
			neighbor = NEIGHBOR.UP;
		else
			neighbor =  NEIGHBOR.KEEP;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusAreaBuffer#userLeft(com.antelope.ci.bus.server.shell.buffer.ShellArea.DIRECTION)
	 */
	@Override
	protected void userLeft(DIRECTION direction) {
		if (direction == DIRECTION.OUTSIDE)
			neighbor = NEIGHBOR.LEFT;
		else
			neighbor =  NEIGHBOR.KEEP;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusAreaBuffer#userRight(com.antelope.ci.bus.server.shell.buffer.ShellArea.DIRECTION)
	 */
	@Override
	protected void userRight(DIRECTION direction) {
		if (direction == DIRECTION.OUTSIDE)
			neighbor = NEIGHBOR.RIGHT;
		else
			neighbor =  NEIGHBOR.KEEP;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusAreaBuffer#move(com.antelope.ci.bus.server.shell.buffer.ShellArea.DIRECTION)
	 */
	@Override protected void move(DIRECTION direction) throws CIBusException {
		try {
			switch (direction) {
				case UP:
					io.moveUp(1);
					area.move(direction);
					break;
				case DOWN:
					io.moveDown(1);
					area.move(direction);
					break;
				case LEFT:
					io.moveLeft(1);
					area.move(direction);
					break;
				case RIGHT:
					io.moveRight(1);
					area.move(direction);
					break;
				default:
					break;
			}
		} catch (IOException e) {
			throw new CIBusException("", "cursor move error", e);
		}
	}
}