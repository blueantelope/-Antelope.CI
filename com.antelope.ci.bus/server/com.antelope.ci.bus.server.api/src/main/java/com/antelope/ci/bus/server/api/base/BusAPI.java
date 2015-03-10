// com.antelope.ci.bus.server.api.base.BusAPI.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.base;

import java.io.IOException;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StreamUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.api.buffer.BusAPIBuffer;
import com.antelope.ci.bus.server.api.message.APIMessage;
import com.antelope.ci.bus.server.common.BusChannel;
import com.antelope.ci.bus.server.common.BusSession;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月7日		下午9:59:43 
 */
public abstract class BusAPI extends BusChannel {
	protected BusAPIBuffer buffer;
	
	public BusAPI() {
		super();
	}
	
	public BusAPI(BusSession session) {
		super(session);
	}
	
	public void initBuffer() {
		buffer.initIO(in, out);
	}
	
	protected void load() throws CIBusException {
		while (true) {
			byte[] bs = new byte[512];
			int index = -1;
			try {
				while ((index=in.read(bs)) != -1) {
					byte[] b = new byte[index];
					System.arraycopy(bs, 0, b, 0, index);
					System.out.println(StreamUtil.toHex(b));
				}
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	protected void recieve(APIMessage message) {
		try {
			buffer.read(message);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	protected void send(APIMessage message) {
		buffer.write(message);
	}
}
