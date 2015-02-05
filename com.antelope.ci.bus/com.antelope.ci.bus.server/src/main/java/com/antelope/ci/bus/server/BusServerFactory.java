// com.antelope.ci.bus.server.BusServerFactory.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月5日		上午9:59:47 
 */
class BusServerFactory {
	private static final Logger log = Logger.getLogger(BusServerFactory.class);
	
	private static BusServerFactory factory = new BusServerFactory();
	
	public static BusServerFactory getFactory() {
		return factory;
	}
	
	private Map<BusServerIdentity, BusServer> serverMap;
	private BusServerFactory() {
		serverMap = new ConcurrentHashMap<BusServerIdentity, BusServer>();
	}
	
	public void startServer(BusServerIdentity identity, BusServer server) throws CIBusException {
		if (!serverMap.containsKey(identity)) {
			serverMap.put(identity, server);
			server.start();
		}
	}
	
	public void addServer(BusServerIdentity identity, BusServer server) {
		if (!serverMap.containsKey(identity))
			serverMap.put(identity, server);
	}
	
	public void start() {
		for (BusServer server : serverMap.values()) {
			try {
				server.start();
			} catch (CIBusException e) {
				log.warn(e);
			}
		}
	}
	
	public void close(BusServerIdentity identity) throws CIBusException {
		if (serverMap.containsKey(identity))
			serverMap.get(identity).close();
	}
	
	public void closeAll() {
		for (BusServer server : serverMap.values()) {
			try {
				server.close();
			} catch (CIBusException e) {
				log.error("Server close failture, " + server.refreshIdentity().toString());
			}
		}
	}
}
