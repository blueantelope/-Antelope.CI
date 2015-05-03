// com.antelope.ci.bus.server.ssh.BusSshSessionCreater.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.BusServerCondition.SERVER_TYPE;
import com.antelope.ci.bus.server.common.BusSession;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月11日		下午3:54:10 
 */
public class BusSshSessionCreater {
	public static BusSession create(SERVER_TYPE serverType, InputStream in, OutputStream out, OutputStream err, 
			ExitCallback callback, Environment env) throws CIBusException {
		switch (serverType) {
			case API:
				return createApiSession(in, out, err, callback, env);
			case SHELL:
				return createShellSession(in, out, err, callback, env);
			default:
				throw new CIBusException("", "unknow server type");
		}
		
	}
	
	protected static BusSshShellSession createShellSession(InputStream in, OutputStream out, OutputStream err, 
			ExitCallback callback, Environment env) {
		return new BusSshShellSession(in, out, err, callback, env);
	}
	
	protected static BusSshApiSession createApiSession(InputStream in, OutputStream out, OutputStream err, 
			ExitCallback callback, Environment env) {
		return new BusSshApiSession(in, out, err, callback, env);
	}
}
