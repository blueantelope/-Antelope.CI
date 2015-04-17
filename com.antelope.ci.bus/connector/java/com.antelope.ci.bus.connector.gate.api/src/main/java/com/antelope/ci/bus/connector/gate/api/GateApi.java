// com.antelope.ci.bus.client.api.gate.GateApi.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.connector.gate.api;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.connector.Attribute;
import com.antelope.ci.bus.connector.ConnectorAdapter;
import com.antelope.ci.bus.connector.IConnector;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月15日		下午4:48:37 
 */
public class GateApi {
	protected IConnector connector;
	
	public GateApi() throws CIBusException {
		super();
		connector = ConnectorAdapter.getConnector(this.getClass());
	}
	
	public void initConnector(Attribute attribute) {
		connector.init(attribute);
	}
}
