// com.antelope.ci.bus.engine.model.BaseModel.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model;

import java.io.Serializable;
import java.util.Map;

import com.antelope.ci.bus.common.Constants;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年12月5日		上午9:28:14 
 */
public abstract class BaseModel implements Serializable {

	public void fromMap(Map<String, String> modelMap) throws CIBusException {
		String className = StringUtil.getLastName(this.getClass().getName(), "//"+Constants.DOT);
		for (String name : modelMap.keySet()) {
			String[] ns = name.split(".");
			if (ns.length == 2) {
				String clsName = ns[0];
				String funName = ns[1];
				funName = "set" + funName.substring(0, 1).toUpperCase() + funName.substring(1); 
				if (clsName.equalsIgnoreCase(className))
					ProxyUtil.invoke(this, funName, new Object[]{modelMap.get(name)});
			}
		}
	}
}

