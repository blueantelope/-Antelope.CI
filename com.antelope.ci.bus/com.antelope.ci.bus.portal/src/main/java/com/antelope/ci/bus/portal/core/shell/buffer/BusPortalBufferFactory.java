// com.antelope.ci.bus.portal.core.shell.BusPortalInputBufferSet.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.buffer;

import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.server.shell.buffer.ShellArea;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年10月10日		下午4:26:25 
 */
public class BusPortalBufferFactory {
	private String name;
	private List<BusPortalInputBuffer> bufferList;
	private BusPortalInputBuffer activeBuffer;
	
	public BusPortalBufferFactory(String name) {
		this.name = name;
		bufferList = new ArrayList<BusPortalInputBuffer>();
	}
	
	public String getName() {
		return name;
	}
	
	public void addBuffer(BusPortalInputBuffer buffer) {
		bufferList.add(buffer);
	}
	
	public List<BusPortalInputBuffer> getBufferList() {
		return bufferList;
	}
	
	public BusPortalInputBuffer getBuffer(String name) {
		for (BusPortalInputBuffer buffer : bufferList) {
			if (buffer.getName().equalsIgnoreCase(name))
				return buffer;
		}
		
		return null;
	}
	
	public void resetBuffer() {
		for (BusPortalInputBuffer buffer : bufferList)
			buffer.reset();
	}
	
	public void initActiveBuffer() {
		if (bufferList.isEmpty())
			return;
		else
			activeBuffer = bufferList.get(0);
		int n = 0;
		int bufsize = bufferList.size();
		for (; n < bufsize; n++) {
			BusPortalInputBuffer rootbuf = bufferList.get(n);
			int m = 0;
			boolean _uped = false;
			boolean _downed = false;
			boolean _lefted = false;
			boolean _righted = false;
			for (; m < bufsize; m++) {
				if (m == n)
					continue;
				BusPortalInputBuffer childbuf = bufferList.get(m);
				// set up for root buffer
				if (!_uped && markUpDownForBuffer(rootbuf, childbuf, 0))
					_uped = true;
				// set down for root buffer
				if (!_downed && markUpDownForBuffer(rootbuf, childbuf, 1))
					_downed = true;
				// set left for root buffer
				if (!_lefted && markLeftRightForBuffer(rootbuf, childbuf, m, n, 0))
					_lefted = true;
				// set right for root buffer
				if (!_righted && markLeftRightForBuffer(rootbuf, childbuf, m, n, 1))
					_righted = true;
			}
		}
	}
	
	private boolean markUpDownForBuffer(BusPortalInputBuffer rootbuf, BusPortalInputBuffer childbuf, int updown) {
		// informations of root buffer
		ShellArea root_area = rootbuf.getArea();
		int root_startx = root_area.getOriginx();
		int root_endx = root_startx + root_area.getWidth();
		int root_endy = root_area.getOriginy() + root_area.getHeight();
		// informations of child buffer
		ShellArea child_area = childbuf.getArea();
		int child_startx = child_area.getOriginx();
		int child_starty = child_area.getOriginy();
		int child_endx = child_startx + child_area.getWidth();
		// set
		if ((updown == 0 && (child_starty < root_endy)) ||
				(updown == 1 && (child_starty > root_endy))) {
			if ((child_startx >= root_startx && child_startx <= root_endx) ||
				(child_endx >= root_startx && child_endx <= root_endx)) {
				if (updown == 0)
					rootbuf.setUp(childbuf);
				else
					rootbuf.setDown(childbuf);
				return true;
			}
		}
		
		return false;
	}
	
	private boolean markLeftRightForBuffer(BusPortalInputBuffer rootbuf, BusPortalInputBuffer childbuf, 
			int rootindex, int childindex, int leftright) {
		// informations of root buffer
		ShellArea root_area = rootbuf.getArea();
		int root_endy = rootbuf.getArea().getOriginy() + root_area.getHeight();
		// informations of child buffer
		ShellArea child_area = childbuf.getArea();
		int child_starty = child_area.getOriginy();
		// set
		if (child_starty == root_endy) {
			if (leftright == 0 && childindex == (rootindex - 1)) {
				rootbuf.setLeft(childbuf);
				return true;
			}
			if (leftright == 1 && childindex == (rootindex + 1)) {
				rootbuf.setRight(childbuf);
				return true;
			}
		}
		
		return false;
	}

	public BusPortalInputBuffer getActiveBuffer() {
		return activeBuffer;
	}
	
	public boolean next() {
		if (activeBuffer != null && activeBuffer.next()) {
			BusPortalInputBuffer nextBuffer = activeBuffer.nextBuffer();
			if (nextBuffer != null) {
				activeBuffer = nextBuffer;
				return true;
			}
		}
		return false;
	}
}