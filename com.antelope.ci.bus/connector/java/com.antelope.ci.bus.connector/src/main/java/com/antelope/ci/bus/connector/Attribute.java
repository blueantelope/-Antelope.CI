// com.antelope.ci.bus.client.Attribute.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.connector;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月15日		下午4:05:12 
 */
public class Attribute {
	protected String host;
	protected int port;
	protected String username;
	protected String password;
	protected long waitForClose;
	protected int bufsize;
	
	public Attribute() {
		super();
		bufsize = 512;
		waitForClose = -1;
	}

	// getter and setter
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

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public long getWaitForClose() {
		return waitForClose;
	}
	public void setWaitForClose(long waitForClose) {
		this.waitForClose = waitForClose;
	}

	public int getBufsize() {
		return bufsize;
	}
	public void setBufsize(int bufsize) {
		this.bufsize = bufsize;
	}
}
