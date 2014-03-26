// com.antelope.ci.bus.portal.dashboard.PortalDashboardEntrance.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.dashboard;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.entrance.CommonEntrance;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-26		下午3:09:59 
 */
public class PortalDashboardEntrance extends CommonEntrance {
	private static final Logger log = Logger.getLogger(PortalDashboardEntrance.class);
	
	@Override
	protected void beforeMount() throws CIBusException {
		log.debug("before mount of dashboard portal");
		
	}

	@Override
	protected void afterMount() throws CIBusException {
		log.debug("after mount of dashboard portal");
		
	}

	@Override
	protected void beforeUnmount() throws CIBusException {
		log.debug("before unmount of dashboard portal");
	}

	@Override
	protected void afterUnmount() throws CIBusException {
		log.debug("after unmount of dashboard portal");
	}

}

