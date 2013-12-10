// com.antelope.ci.bus.server.shell.ShellUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.io.IOException;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-9		下午5:23:32 
 */
public class ShellUtil {
	
	public static void clear(TerminalIO io) throws CIBusException {
		try {
			io.eraseScreen ();
			io.homeCursor ();
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
}

