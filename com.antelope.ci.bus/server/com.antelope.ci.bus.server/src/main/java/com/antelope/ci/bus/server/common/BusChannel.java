// com.antelope.ci.bus.server.common.BusChannel.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		下午4:56:51 
 */
public abstract class BusChannel {
	protected BusSession session;
	protected InputStream in;
	protected OutputStream out;
	protected OutputStream err;
	protected boolean opened;
	protected boolean quit;
	
	public BusChannel() {
		opened = false;
		quit = false;
		init();
	}
	
	public BusChannel(BusSession session) {
		this();
		this.session = session;
	}
	
	public void attatchSession(BusSession session) {
		this.session = session;
	}
	
	public BusSession getSession() {
		return session;
	}
	
	public void open() throws CIBusException {
		opened = true;
		environment();
		load();
	}
	
	private void environment() throws CIBusException {
		in = session.getIn();
		out = session.getOut();
		err = session.getErr();
		customEnv();
	}
	
	
	public void close() throws CIBusException {
		release();
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
		if (err != null) {
			try {
				err.close();
			} catch (IOException e) {
			}
		}
		session.exit();
	}
	
	public ClassLoader getClassLoader() {
		return this.getClass().getClassLoader();
	}
	
	protected abstract void init();
	
	protected abstract void customEnv() throws CIBusException;
	
	protected abstract void load() throws CIBusException;
	
	protected abstract void release() throws CIBusException;
	
	public abstract void setContext(Object... contexts);
}
