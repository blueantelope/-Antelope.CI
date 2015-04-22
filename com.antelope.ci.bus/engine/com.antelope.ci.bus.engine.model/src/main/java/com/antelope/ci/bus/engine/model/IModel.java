// com.antelope.ci.bus.engine.model.IModel.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model;

import com.antelope.ci.bus.common.api.ApiMessage;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月16日		下午3:26:30 
 */
public interface IModel {
	public String toJson();
	
	public IModel fromJson(String json);
	
	public ApiMessage toMessage();
	
	public IModel fromMessage(ApiMessage message);
}
