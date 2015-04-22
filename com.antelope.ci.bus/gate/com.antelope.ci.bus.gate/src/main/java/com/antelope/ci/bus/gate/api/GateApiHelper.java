// com.antelope.ci.bus.gate.api.CommonGateApi.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.api;

import com.antelope.ci.bus.common.api.BT;
import com.antelope.ci.bus.engine.model.IModel;
import com.antelope.ci.bus.gate.api.message.GateInMessage;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月21日		上午10:09:18 
 */
public class GateApiHelper {
	
	protected static void toModel(GateInMessage in, IModel model) {
		switch (in.getBt()) {
			case BT._json:
				in.getBody();
				model.fromMessage(in);
				break;
			case BT._binary:
			default:
					
				break;
		}
	}

}
