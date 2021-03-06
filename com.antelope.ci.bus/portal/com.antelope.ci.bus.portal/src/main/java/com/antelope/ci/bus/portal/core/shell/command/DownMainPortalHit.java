// com.antelope.ci.bus.portal.core.shell.command.DownMainPortalHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.command;

import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.server.shell.base.BusShellMode;
import com.antelope.ci.bus.server.shell.base.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandHelper;
import com.antelope.ci.bus.server.shell.command.CommandType;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-26		上午11:28:31 
 */
@Command(
		name="down.main", 
		commands=CommandHelper.downCommand, 
		status=BusShellStatus.GLOBAL, 
		type=CommandType.HIT, 
		mode=BusShellMode.MAIN)
public class DownMainPortalHit extends PortalHit {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.portal.core.shell.command.PortalHit#invoke(com.antelope.ci.bus.portal.core.shell.BusPortalShell, java.lang.Object[])
	 */
	@Override protected String invoke(BusPortalShell shell, Object... args) {
		down(shell);
		return BusShellStatus.KEEP;
	}
}