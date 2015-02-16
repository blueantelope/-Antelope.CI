// com.antelope.ci.bus.server.BusServerIdentity.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import com.antelope.ci.bus.common.PROTOCOL;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月5日		上午10:12:41 
 */
public class BusServerIdentity {
	protected String host;
	protected int port;
	protected PROTOCOL proto;
	protected String summary;
	
	// getter and setting
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public PROTOCOL getProto() {
		return proto;
	}
	public void setProto(PROTOCOL proto) {
		this.proto = proto;
	}
	
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "server run:{" + host + ":" + port + " on " + proto.getName() + "}, " + summary;
	}
	
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object another) {
		if (super.equals(another))
			return true;
		if (another instanceof BusServerIdentity) {
			BusServerIdentity identity = (BusServerIdentity) another;
			if (host.equals(identity.getHost()) 
					&& port == identity.getPort() 
					&& proto == identity.getProto())
				return true;
		}
		
		return false;
	}
}

