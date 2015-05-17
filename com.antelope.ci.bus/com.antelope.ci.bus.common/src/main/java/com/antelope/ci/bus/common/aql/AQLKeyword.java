// com.antelope.ci.bus.common.aql.AqlLogic.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.aql;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月16日		下午4:33:14 
 */

public enum AQLKeyword {
	AND(new String[]{"and", "&&"}, "and"),
	OR(new String[]{"or", "|"}, "or"),
	LIKE(new String[]{"like"}, "like"),
	NOT(new String[]{"not", "!"}, "not"),
	EQUALS(new String[]{"equals", "="}, "equals"),
	NOTEQUALS(new String[]{"not equals", "!="}, "not equals");

	private String codes[];
	private String description;
	private AQLKeyword(String[] codes, String description) {
		this.codes = codes;
		this.description = description;
	}
	
	public String[] getCodes() {
		return codes;
	}
	
	@Override
	public String toString() {
		String str = "keyword : ";
		for (String code : codes) {
			str += code + ",";
		}
		if (codes.length > 0)
			str = str.substring(0, str.length()-1);
		str += "}";
		return str;
	}
	
	public static AQLKeyword fromCode(String code) throws CIBusException {
		if (StringUtil.empty(code))
			throw new CIBusException("", "aqlogic code null");
		
		for (AQLKeyword keyword : AQLKeyword.values()) {
			for (String kcode : keyword.getCodes()) {
				if (code.trim().equalsIgnoreCase(kcode))
					return keyword;
			}
		}
		
		throw new CIBusException("", "unknown aql keyword : " + code);
	}
}

