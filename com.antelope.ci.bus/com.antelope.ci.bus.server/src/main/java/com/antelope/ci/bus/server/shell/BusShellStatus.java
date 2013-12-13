// com.antelope.ci.bus.server.shell.command.CommandStatus.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		下午12:27:49 
 */
public class BusShellStatus {
	public static final String INIT 			= "command.status.init";
	
	public static final String ROOT 			= "command.status.root";
	public static final int ROOT_CODE 	= 1;
	
	public static final String HELP 			= "command.status.help";
	public static final int HELP_CODE 		= 2;
	
	public static final String QUIT 			= "command.status.quit";
	public static final int QUIT_CODE 		= 3;
	
	public static final String LAST 			= "command.status.last";					// last time status
	public static final int LAST_CODE	 	= 4;
	
	public static final String KEEP 			= "command.status.keep";					// current status, not to change
	public static final int KEEP_CODE 		= 5;
	
	public static int hash(String status) {
		if (status.equals(ROOT))
			return ROOT_CODE;
		if (status.equals(HELP))
			return HELP_CODE;
		if (status.equals(QUIT))
			return QUIT_CODE;
		if (status.equals(LAST))
			return LAST_CODE;
		if (status.equals(KEEP))
			return KEEP_CODE;
		
		return -1;
	}
}

