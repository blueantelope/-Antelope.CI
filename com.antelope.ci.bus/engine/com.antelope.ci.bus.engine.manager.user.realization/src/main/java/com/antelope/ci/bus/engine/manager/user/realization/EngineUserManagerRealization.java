// com.antelope.ci.bus.engine.manager.user.realization.EngineUserManagerRealization.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager.user.realization;

import com.antelope.ci.bus.engine.manager.EngineManager;
import com.antelope.ci.bus.engine.manager.EngineService;
import com.antelope.ci.bus.engine.manager.user.CommonEngineUserManager;
import com.antelope.ci.bus.engine.manager.user.EngineUserManagerConstants;




/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月30日		下午2:38:22 
 */
@EngineService
@EngineManager(service_name=EngineUserManagerConstants.SERVICE_NAME)
public class EngineUserManagerRealization extends CommonEngineUserManager {

}
