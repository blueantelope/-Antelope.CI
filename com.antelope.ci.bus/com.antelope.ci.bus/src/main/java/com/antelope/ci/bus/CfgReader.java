// com.antelope.ci.bus.BusCnf.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus;

import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.configration.CfgFileReader;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 配置文件读取
 * 取得配置文件的属性
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		下午9:12:33 
 */
public class CfgReader {
	// singleton
	private static final CfgReader cfg = new CfgReader();
	
	public static final CfgReader getCfg() {
		return cfg;
	}
	
	private BasicConfigrationReader reader;
	private CfgReader() {
		reader = new CfgFileReader();
	}
	
	/*
	 * 加载配置文件
	 */
	void loadCnf(String path) throws CIBusException {
		reader.addConfig(path);
	}
	
	/**
	 * 取得配置中的参数读取类
	 * @param  @return
	 * @return BasicConfigrationReader
	 * @throws
	 */
	public BasicConfigrationReader getConfigration() {
		return reader;
	}
}

