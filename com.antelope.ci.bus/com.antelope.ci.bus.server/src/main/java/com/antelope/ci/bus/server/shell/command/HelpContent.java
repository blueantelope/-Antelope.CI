// com.antelope.ci.bus.server.shell.command.HelpContent.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import java.io.InputStream;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		下午3:54:28 
 */
public class HelpContent {
	private static final String HELP_XML= "/commands/help.xml";
	private static final HelpContent content = new HelpContent();
	
	public static final HelpContent getContent() {
		return content;
	}
	
	private Help help;
	
	private HelpContent() {
		try {
			init();
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	private void init() throws CIBusException {
		InputStream in =Help.class.getResourceAsStream(HELP_XML);
		refresh(in);
	}
	
	private void refresh() throws CIBusException {
		init();
	}
	
	public void refresh(InputStream in) throws CIBusException {
		help =  (Help) BusXmlHelper.parse(Help.class, in);
	}
	
	public String getContent(String type, String status) {
		String content = null;
		if (help.getTypeList() != null) {
			for (HelpType tc : help.getTypeList()) {
				if (tc.getName().equalsIgnoreCase(type)) {
					if (tc != null) {
						for (HelpTypeStatus sc : tc.getStatusList()) {
							if (sc.getName().equalsIgnoreCase(status))
								content = sc.getContent();
						}
					}
					break;
				}
			}
		}
		
		return content;
	}
	
	public String getEchoContent(String status) {
		return getContent(CommandType.ECHO.getShell(), status);
	}
	
	public String getFrameContent(String status) {
		return getContent(CommandType.Hit.getShell(), status);
	}
}

