// com.antelope.ci.bus.client.Client.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.connector;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月15日		下午4:01:56 
 */
public interface IConnector {
	public void connect() throws CIBusException;
	
	public byte[] read() throws CIBusException;

	public void send(byte[] datas) throws CIBusException;

	public void close() throws CIBusException;
}
