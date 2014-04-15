// com.antelope.ci.bus.portal.shell.command.PortalHitAdapter.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.shell.command;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.command.hit.HitAdapter;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-14		下午4:43:28 
 */
public class PortalCommandAdapter extends HitAdapter {
	public PortalCommandAdapter() {
		super();
		try {
			init_add();
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	private void init_add() throws CIBusException {
		addCommands("com.antelope.ci.bus.portal.shell.command");
	}
}

