// com.antelope.ci.bus.server.shell.portal.PortalAdapter.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.shell.command.hit;

import com.antelope.ci.bus.shell.command.CommandAdapter;
import com.antelope.ci.bus.shell.command.CommandType;

/**
 * portal命令适配器
 * 所有portal 命令调用由此适配器进入
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-2		下午5:15:36 
 */
public abstract class HitAdapter extends CommandAdapter {
	public HitAdapter() {
		super(CommandType.HIT);
	}

	@Override
	protected void init() {
		addCommand(new HelpHit());
		addCommand(new QuitHit());
		addCommand(new RefreshHit());
	}
}