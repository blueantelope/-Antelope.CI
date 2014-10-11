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


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年10月10日		下午4:26:25 
 */
public class BusPortalFormBuffer {
	private String name;
	private List<BusPortalInputBuffer> bufferList;
	
	public BusPortalFormBuffer(String name) {
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
		for (BusPortalInputBuffer buffer : bufferList) {
			buffer.reset();
		}
	}
}

