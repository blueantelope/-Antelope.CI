// com.antelope.ci.bus.portal.core.shell.BusPortalInputBufferSet.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.buffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.antelope.ci.bus.server.shell.buffer.BusShellBuffer;
import com.antelope.ci.bus.server.shell.buffer.BusShellHitBuffer;
import com.antelope.ci.bus.server.shell.buffer.ShellArea;
import com.antelope.ci.bus.server.shell.util.TerminalIO;


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
	private BusShellBuffer command;
	
	public BusPortalBufferFactory(String name, TerminalIO io) {
		this.name = name;
		bufferList = new ArrayList<BusPortalInputBuffer>();
		command = new BusShellHitBuffer(io);
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
			if (rootbuf.fullNeighbors())
				continue;
			int m = 0;
			for (; m < bufsize; m++) {
				if (m == n)
					continue;
				BusPortalInputBuffer childbuf = bufferList.get(m);
				// set up for root buffer
				if (!rootbuf.isMarkUp())
					markUpDownForBuffer(rootbuf, childbuf, 0);
				// set down for root buffer
				if (!rootbuf.isMarkDown())
					markUpDownForBuffer(rootbuf, childbuf, 1);
				// set left for root buffer
				if (!rootbuf.isMarkLeft())
					markLeftRightForBuffer(rootbuf, childbuf, m, n, 0);
				// set right for root buffer
				if (!rootbuf.isMarkRight())
					markLeftRightForBuffer(rootbuf, childbuf, m, n, 1);
			}
		}
	}
	
	private void markUpDownForBuffer(BusPortalInputBuffer rootbuf, BusPortalInputBuffer childbuf, int updown) {
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
				if (updown == 0) {
					rootbuf.setUp(childbuf);
					childbuf.setDown(rootbuf);
				} else {
					rootbuf.setDown(childbuf);
					childbuf.setUp(rootbuf);
				}
			}
		}
	}
	
	private void markLeftRightForBuffer(BusPortalInputBuffer rootbuf, BusPortalInputBuffer childbuf, 
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
				childbuf.setRight(rootbuf);
			}
			if (leftright == 1 && childindex == (rootindex + 1)) {
				rootbuf.setRight(childbuf);
				childbuf.setLeft(rootbuf);
			}
		}
	}

	public BusPortalInputBuffer getActiveBuffer() {
		return activeBuffer;
	}
	
	public boolean next() {
		if (activeBuffer != null && activeBuffer.goNeighbor()) {
			BusPortalInputBuffer nextBuffer = activeBuffer.nextBuffer();
			if (nextBuffer != null) {
				activeBuffer = nextBuffer;
				return true;
			}
		}
		return false;
	}
	
	public BusShellBuffer initCommand() {
		command.reset();
		return command;
	}
	
	public BusShellBuffer getCommand() {
		return command;
	}
	
	public Map<String, String> getBufferContents() {
		Map<String, String> contents = new HashMap<String, String>();
		for (BusPortalInputBuffer buffer : bufferList) {
			contents.put(buffer.getName(), buffer.toString());
		}
		
		return contents;
	}
}