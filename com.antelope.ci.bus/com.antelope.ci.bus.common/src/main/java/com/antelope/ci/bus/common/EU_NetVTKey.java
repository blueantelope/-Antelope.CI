// com.antelope.ci.bus.common.NetVTKey_ENUM.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-15		下午4:50:40 
 */
public enum EU_NetVTKey {
	BEL(7, (char) 7),
	BS (8, (char) 8),
	DEL(127, (char) 127),
	CR (13, (char) 13),
	LF (10, (char) 10),
	SPACE(32, (char) 32),
	UP(1001, (char) 32),
	TABULATOR(1301, (char) 9);
	
	
	private int vtkey;
	private char c;
	private EU_NetVTKey(int vtkey, char c) {
		this.vtkey = vtkey;
		this.c = c;
	}
	
	public int getVtkey() {
		return vtkey;
	}
	
	public char getC() {
		return c;
	}
	
	@Override public String toString() {
		return "vt=" + vtkey + ", char=" + c;
	}
	
	public static EU_NetVTKey fromVtkey(int vtkey) {
		for (EU_NetVTKey en : EU_NetVTKey.values())
			if (en.getVtkey() == vtkey)
				return en;
		
		return null;
	}
	
	public static EU_NetVTKey fromChar(char c) {
		for (EU_NetVTKey en : EU_NetVTKey.values())
			if (en.getC() == c)
				return en;
		
		return null;
	}
}

