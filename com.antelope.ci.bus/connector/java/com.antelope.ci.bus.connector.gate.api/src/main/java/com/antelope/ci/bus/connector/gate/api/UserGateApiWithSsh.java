// com.antelope.ci.bus.client.api.gate.UserGateApiWithSsh.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.connector.gate.api;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.connector.Invoker;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月15日		下午4:51:33 
 */
@Invoker(connectName="ssh")
public class UserGateApiWithSsh extends UserGateApi {
	public UserGateApiWithSsh() throws CIBusException {
		super();
	}
}
