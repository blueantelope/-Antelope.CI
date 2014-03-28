// com.antelope.ci.bus.portal.shell.command.SwitchShellHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.shell.command;

import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandType;
import com.antelope.ci.bus.server.shell.command.hit.Hit;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-28		下午5:37:11 
 */
@Command(name="switch_portal", commands="\t", status=BusShellStatus.ROOT, type=CommandType.HIT, beforeClear=true)
public class SwitchPortalHit extends Hit {

	@Override
	protected String execute(TerminalIO io, Object... args) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

}

