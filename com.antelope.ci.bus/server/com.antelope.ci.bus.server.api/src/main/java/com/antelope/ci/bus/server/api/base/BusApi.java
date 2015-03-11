// com.antelope.ci.bus.server.api.base.BusAPI.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.base;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StreamUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.api.message.ApiMessage;
import com.antelope.ci.bus.server.common.BusChannel;
import com.antelope.ci.bus.server.common.BusSession;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月7日		下午9:59:43 
 */
public abstract class BusApi extends BusChannel {
	protected BusApiBuffer buffer;
	protected ApiIO io;
	
	public BusApi() {
		super();
	}
	
	public BusApi(BusSession session) {
		super(session);
	}
	
	@Override
	protected void customEnv() throws CIBusException {
		BusApiSession apiSession = (BusApiSession) session;
		io = apiSession.getIo();
		buffer = new BusApiBuffer(io);
		customApiEnv();
	}
	
	protected void load() throws CIBusException {
		byte[] bs = new byte[512];
		int index = -1;
		while (true) {
			try {
				while ((index=io.read(bs)) != -1) {
					byte[] b = new byte[index];
					System.arraycopy(bs, 0, b, 0, index);
					System.out.println(StreamUtil.toHex(b));
				}
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	protected void recieve(ApiMessage message) {
		try {
			buffer.read(message);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	protected void send(ApiMessage message) {
		buffer.write(message);
	}
	
	protected abstract void customApiEnv() throws CIBusException;
}
