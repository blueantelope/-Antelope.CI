// com.antelope.ci.bus.portal.shell.PartCursor.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.shell;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-27		下午5:48:56 
 */
public class PartCursor {
	int part_x;
	int part_y;
	public PartCursor() {
		super();
		init(0, 0);
	}
	public PartCursor(int part_x, int part_y) {
		super();
		init(part_x, part_y);
	}
	private void init(int part_x, int part_y) {
		this.part_x = part_x;
		this.part_y = part_y;
	}
	public int getPart_x() {
		return part_x;
	}
	public int getPart_y() {
		return part_y;
	}
	public void setPart_x(int part_x) {
		this.part_x = part_x;
	}
	public void setPart_y(int part_y) {
		this.part_y = part_y;
	}
	public void addPart_y(int times) {
		this.part_y += times;
	}
}

