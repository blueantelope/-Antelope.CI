// com.antelope.ci.bus.BusCnf.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus;

import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.configration.CnfFileReader;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 配置文件读取
 * 取得配置文件的属性
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		下午9:12:33 
 */
class CnfReader {
	// singleton
	private static final CnfReader cnf = new CnfReader();
	
	public static final CnfReader getCnf() {
		return cnf;
	}
	
	private BasicConfigrationReader reader;
	private CnfReader() {
		reader = new CnfFileReader();
	}
	
	/*
	 * 加载bus.cnf
	 */
	void loadCnf(String path) throws CIBusException {
		reader.addResource(path);
	}
}

