// com.antelope.ci.bus.portal.core.shell.command.CommandMainPortalHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.command;

import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.PortalBlock;
import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.Command;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年9月29日		下午3:16:25 
 */
public class RunMainPortalHit extends PortalHit {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.portal.core.shell.command.PortalHit#invoke(com.antelope.ci.bus.portal.core.shell.BusPortalShell, java.lang.Object[])
	 */
	@Override protected String invoke(BusPortalShell shell, Object... args) {
		Command command = getContent();
		PortalBlock block = shell.getActiveBlock();
		if (block != null) {
			String name = command.status() + "."  + command.mode() + "." + block.getName();
			shell.runCommand(name);
		}
		
		return BusShellStatus.KEEP;
	}

}

