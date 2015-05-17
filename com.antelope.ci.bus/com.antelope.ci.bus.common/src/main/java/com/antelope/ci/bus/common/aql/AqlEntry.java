// com.antelope.ci.bus.common.aql.AqiEntity.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.aql;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月16日		下午4:30:12 
 */
public class AqlEntry {
	private String name;
	private String value;
	private AQLKeyword keyword;
	
	public AqlEntry() {
		super();
	}
	
	public AqlEntry(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	// getter ans setter
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public AQLKeyword getKeyword() {
		return keyword;
	}
	public void setKeyword(AQLKeyword keyword) {
		this.keyword = keyword;
	}
	public void codeToKeyword(String code) throws CIBusException {
		this.keyword = AQLKeyword.fromCode(code);
	}
}

