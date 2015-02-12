// com.antelope.ci.bus.server.shell.buffer.ShellFrame.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.shell.buffer;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年10月14日		下午12:21:29 
 */
public class ShellArea {
	public enum DIRECTION{OUTSIDE, UP, DOWN, LEFT, RIGHT, LEFT_UP, RIGTH_DOWN};
	
	private ShellCursor origin;
	private ShellCursor position;
	private ShellCursor limit;
	private ShellCursor capacity;
	private int width;
	private int height;
	
	public ShellArea(ShellCursor origin, int width, int height) {
		super();
		this.origin = origin;
		this.width = width;
		this.height = height;
		init();
	}
	
	private void init() {
		position = origin.clone();
		limit = origin.clone();
		capacity = new ShellCursor(origin.getX()+width, origin.getY()+height);
	}
	
	public void reset() {
		init();
	}
	
	
	public int getOriginx() {
		return origin.getX();
	}
	public int getOriginy() {
		return origin.getY();
	}
	
	public int getPositionx() {
		return position.getX();
	}
	public int getPositiony() {
		return position.getY();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public DIRECTION directMoveUp() {
		return directUp(position);
	}
	
	public DIRECTION directMoveDown() {
		return directDown(position, limit);
	}
	
	public DIRECTION directMovetLeft() {
		return directLeft(position);
	}
	
	public DIRECTION directMoveRight() {
		return directRight(position, limit);
	}
	
	public void move(DIRECTION direction) {
		switch (direction) {
			case UP:
				position.up(1);
				break;
			case DOWN:
				position.down(1);
				break;
			case LEFT:
				position.left(1);
				break;
			case RIGHT:
				position.right(1);
				break;
			case LEFT_UP:
				position.setX(origin.getX()+width);
				position.up(1);
				break;
			case RIGTH_DOWN:
				position.setX(0);
				position.down(1);
				break;
			default:
				break;
		}
	}
	
	public int toLineEnd() {
		return origin.getX() + width - position.getX();
	}
	
	public int index() {
		return distance(position, origin);
	}
	
	public int headlineIndex() {
		return (position.getY() - origin.getY()) * width;
	}
	
	public int distanceToLimit() {
		return distance(limit, position);
	}
	
	public int linesToLimit() {
		int distance = distance(limit, position);
		return distance / width;
	}
	
	public int distanceFromOrigin(int x, int y) {
		return distance(new ShellCursor(x, y), origin);
	}
	
	public int distanceToLimit(int x, int y) {
		return distance(limit, new ShellCursor(x, y));
	}
	
	public void go() {
		right(limit, capacity);
		right(position, limit);
	}
	
	public void go(int times) {
		int n = 0;
		while (n < times) {
			go();
			n++;
		}
	}
	
	public void back() {
		left(limit);
		left(position);
	}
	
	public void back(int times) {
		int n = 0;
		while (n < times) {
			back();
			n++;
		}
	}
	
	public void decrease(int times) {
		int n = 0;
		while (n < times) {
			left(limit);
			n++;
		}
	}
	
	public DIRECTION directLeft(ShellCursor v_position) {
		if (v_position.coincident(origin))
			return DIRECTION.OUTSIDE;
		
		if (v_position.getX() == origin.getX())
			return DIRECTION.LEFT_UP;
		else
			return DIRECTION.LEFT;
	}
	
	public DIRECTION directRight(ShellCursor position, ShellCursor limit) {
		if (position.coincident(limit))
			return DIRECTION.OUTSIDE;
		
		if (position.getX() == capacity.getX())
			return DIRECTION.RIGTH_DOWN;
		else
			return DIRECTION.RIGHT;
	}
	
	public boolean locateStart() {
		return position.coincident(origin);
	}
	
	public boolean locateEnd() {
		return position.coincident(capacity);
	}
	
	public boolean atEnd() {
		return limit.coincident(capacity);
	}
	
	public boolean atLimit() {
		return position.coincident(limit);
	}
	
	public boolean newLine() {
		return !locateStart() && (position.getX() == 0);
	}
	
	private void right(ShellCursor v_position, ShellCursor v_limit) {
		switch (directRight(v_position, v_limit)) {
			case RIGHT:
				v_position.right(1);
				break;
			case RIGTH_DOWN:
				v_position.setX(0);
				v_position.down(1);
				break;
			default:
				break;
		}
	}
	
	private void left(ShellCursor v_position) {
		switch (directLeft(v_position)) {
			case LEFT:
				v_position.left(1);
				break;
			case LEFT_UP:
				v_position.setX(origin.getX()+width);
				v_position.up(1);
				break;
			default:
				break;
		}
	}
	
	private DIRECTION directUp(ShellCursor v_position) {
		if (v_position.getY() > origin.getY())
			return DIRECTION.UP;
		return DIRECTION.OUTSIDE;
	}
	
	private DIRECTION directDown(ShellCursor v_position, ShellCursor v_limit) {
		if (v_position.getY() < v_limit.getY())
			return DIRECTION.DOWN;
		return DIRECTION.OUTSIDE;
	}
	
	private int distance(ShellCursor position_end, ShellCursor position_start) {
		return (position_end.getY() * width + position_end.getX()) -  (position_start.getY() * width + position_start.getX());
	}
}