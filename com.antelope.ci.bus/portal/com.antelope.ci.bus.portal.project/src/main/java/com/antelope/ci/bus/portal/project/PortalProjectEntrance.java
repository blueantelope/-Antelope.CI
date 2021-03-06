// com.antelope.ci.bus.portal.project.PortalProjectEntrance.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.entrance.CommonEntrance;
import com.antelope.ci.bus.portal.core.entrance.PortalEntrance;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-18		下午10:44:38 
 */
@PortalEntrance
public class PortalProjectEntrance extends CommonEntrance {
	private static final Logger log = Logger.getLogger(PortalProjectEntrance.class);

	@Override
	protected void beforeMount() throws CIBusException {
		log.debug("before mount of project portal");
		
	}

	@Override
	protected void afterMount() throws CIBusException {
		log.debug("after mount of project portal");
		
	}

	@Override
	protected void beforeUnmount() throws CIBusException {
		log.debug("before unmount of project portal");
		
	}

	@Override
	protected void afterUnmount() throws CIBusException {
		log.debug("after unmount of project portal");
		
	}
}
