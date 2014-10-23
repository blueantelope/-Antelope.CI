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


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年10月10日		下午4:20:59 
 */
public class BusPortalInputBuffer extends BusInputBuffer {
	private String name;
	private BusPortalShell shell;
	
	public BusPortalInputBuffer(BusPortalShell shell, int x, int y, int width, int height, String name) {
		super(shell.getIO(), x, y, width, height);
		this.name = name;
		this.shell = shell;
	}

	
	public String getName() {
		return name;
	}

	@Override
	protected void rewriteAhead(int x, int y) {
		shell.commomIO();
		BusPortalShellLiving shellLiving = shell.getLiving();
		List<BusPortalShellUnit> units = shellLiving.getAheadLine(x, y);
		shell.rewriteUnits(units);
		shell.editIO();
	}

	@Override
	protected void rewriteLatter(int x, int y) {
		
		// TODO Auto-generated method stub
		
	}
}

