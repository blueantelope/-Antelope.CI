// com.antelope.ci.bus.server.shell.command.BusHelpCommand.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import java.io.IOException;
import java.io.InputStream;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;
import com.antelope.ci.bus.server.shell.core.TerminalIO;



/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-25		下午9:00:24 
 */
@ServerCommand(name="help", commands="help")
public class HelpCommand implements Command {
	protected static final String HELP_XML= "/commands/help.xml";
	protected String content;
	private boolean exception = false;

	public HelpCommand() {
		try {
			init();
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
			exception = true;
		}
	}
	
	protected void init() throws CIBusException {
		InputStream in = this.getClass().getResourceAsStream(HELP_XML);
		HelpContent helpContnet = (HelpContent) BusXmlHelper.parse(HelpContent.class, in);
		content = helpContnet.getContent();
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.Command#execute(com.antelope.ci.bus.server.shell.core.TerminalIO, java.lang.Object[])
	 */
	@Override
	public void execute(TerminalIO io, Object... args) throws CIBusException {
		if (!exception) {
			try {
				io.println(content);
			} catch (IOException e) {
				throw new CIBusException("", e);
			}
		}
	}
}

