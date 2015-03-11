// com.antelope.ci.bus.server.api.base.ApiIO.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.base;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月11日		下午2:46:27 
 */
public class ApiIO {
	protected DataInputStream m_In;
	protected DataOutputStream m_Out;
	
	public ApiIO(InputStream in, OutputStream out) {
		this.m_In = new DataInputStream(in);
		this.m_Out = new DataOutputStream(out);
	}
	
	public void write(byte[] b) throws CIBusException {
		try {
			m_Out.write(b);
		} catch (IOException e) {
			throw new CIBusException("", "Api IO write error", e);
		}
	}
	
	public int read(byte[] b) throws CIBusException {
		try {
			return m_In.read(b);
		} catch (IOException e) {
			throw new CIBusException("", "Api IO read error", e);
		}
	}
}

