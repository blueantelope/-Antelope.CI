// com.antelope.ci.bus.portal.core.shell.command.MainCommonPortalHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.command;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.PortalBlock;
import com.antelope.ci.bus.portal.core.shell.PortalShellUtil;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-26		上午11:04:50 
 */
public abstract class MainCommonPortalHit extends PortalHit {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.BaseCommand#execute(com.antelope.ci.bus.server.shell.BusShell, com.antelope.ci.bus.server.shell.core.TerminalIO, java.lang.String, java.lang.Object[])
	 */
	@Override protected String execute(BusShell shell, TerminalIO io, String status, Object... args) {
		if (PortalShellUtil.isMainMode((BusPortalShell) shell))
			return executeOnMain((BusPortalShell) shell, io, status, args);
		return BusShellStatus.KEEP;
	}
	
	protected abstract String executeOnMain(BusPortalShell shell, TerminalIO io, String status, Object... args);
	
	protected void up(BusPortalShell shell) {
		move(shell, 1);
	}
	
	protected void down(BusPortalShell shell) {
		move(shell, 2);
	}
	
	protected void left(BusPortalShell shell) {
		move(shell, 3);
	}
	
	protected void right(BusPortalShell shell) {
		move(shell, 4);
	}
	
	private void move(BusPortalShell shell, int direction) {
		PortalBlock block = shell.getActiveBlock();
		if (null == block)	return;
		PortalBlock moveBlock = null;
		switch (direction) {
			case 1:
				moveBlock = block.getUp();
				break;
			case 2:
				moveBlock = block.getDown();
				break;
			case 3:
				moveBlock = block.getLeft();
				break;
			case 4:
				moveBlock = block.getRight();
				break;
		}
		if (moveBlock != null) {
			try {
				shell.moveContent();
				shell.move(moveBlock.getCursor().getX(), moveBlock.getCursor().getY());
				shell.updateBlock(moveBlock);
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
			}
		}
	}
}

