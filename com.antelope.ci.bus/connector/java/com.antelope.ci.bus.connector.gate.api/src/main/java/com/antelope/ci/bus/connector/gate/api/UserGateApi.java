// com.antelope.ci.bus.client.gate.api.UserApi.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.connector.gate.api;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.connector.ConnectorAdapter;
import com.antelope.ci.bus.connector.IConnector;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月15日		下午4:43:32 
 */
public class UserGateApi extends GateApi {
	private IConnector connector;
	
	public UserGateApi() throws CIBusException {
		super();
		connector = ConnectorAdapter.getConnector(this.getClass());
	}
	
	public byte[] getUser() throws CIBusException {
		connector.connect();
		
		return null;
	}
}
