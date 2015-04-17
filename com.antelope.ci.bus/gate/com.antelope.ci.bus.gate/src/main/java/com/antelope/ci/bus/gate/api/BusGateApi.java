// com.antelope.ci.bus.gate.api.BusGateAPI.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api;

import java.util.Map;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.api.ApiMessage;
import com.antelope.ci.bus.common.api.OT;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.gate.GateApiScanner;
import com.antelope.ci.bus.gate.api.message.GateInMessage;
import com.antelope.ci.bus.gate.api.message.GateOutMessage;
import com.antelope.ci.bus.server.api.base.BusApi;
import com.antelope.ci.bus.server.common.BusSession;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月9日		上午10:59:05 
 */
public class BusGateApi extends BusApi {
	private final static Logger log = Logger.getLogger(BusGateApi.class);
	protected Map<Short, IGateApi> apiMap;
	
	public BusGateApi() {
		super();
	}
	
	public BusGateApi(BusSession session) {
		super(session);
	}
	
	public void setApiMap(Map<Short, IGateApi> apiMap) {
		this.apiMap = apiMap;
	}

	@Override
	protected void customApiEnv() throws CIBusException {
		
	}

	@Override
	protected void init() {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void release() throws CIBusException {
		
		
	}

	@Override
	protected void handleInMessage(ApiMessage message) {
		IGateApi api = GateApiScanner.getScanner().getGateApi(message.getOc());
		if (api != null) {
			GateInMessage inMessage = new GateInMessage();
			inMessage.clone(message);
			GateOutMessage outMessage;
			switch (message.getOt()) {
				case OT._ls:
					outMessage = api.ls(inMessage);
					break;
				case OT._add:
					outMessage = api.add(inMessage);
					break;
				case OT._delete:
					outMessage = api.delete(inMessage);
					break;
				case OT._mod:
					outMessage = api.mod(inMessage);
					break;
				default:
					log.warn("invalidate OT(operation type) : " + message.getOt());
					break;
			}
		}
	}
}
