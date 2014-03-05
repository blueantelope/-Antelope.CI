// com.antelope.ci.bus.portal.entrance.CommonEntrance.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.entrance;

import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.BusCommonServerActivator;
import com.antelope.ci.bus.server.BusServerCondition;
import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.Shell;
import com.antelope.ci.bus.server.shell.StatusClass;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-17		下午4:09:19 
 */
public abstract class CommonEntrance implements Entrance {
	private static final Logger log = Logger.getLogger(CommonEntrance.class);
	protected BusServerCondition server_condition = null;
	
	@Override
	public void init(Object... args) throws CIBusException {
		for (Object arg : args) {
			if (arg instanceof BusServerCondition)
				this.server_condition = (BusServerCondition) arg;
		}
	}
	
	@Override
	public void mount() throws CIBusException {
		log.debug("start for portal mount");
		beforeMount();
		initMount();
		afterMount();
		log.debug("end for portal mount");
	}
	
	@Override
	public void unmount() throws CIBusException {
		log.debug("start for portal unmount");
		beforeUnmount();
		destroy();
		afterUnmount();
		log.debug("end for portal unmount");
	}
	
	private void initMount() throws CIBusException {
		// mount shell status class
		List<String> classList = ClassFinder.findClasspath(this.getClass().getPackage().getName(), 
				BusCommonServerActivator.getClassLoader());
		for (String cls : classList) {
			try {
				Class clz = Class.forName(cls, false, BusCommonServerActivator.getClassLoader());
				if (clz.isAnnotationPresent(Shell.class)) {
					if (server_condition != null)
						server_condition.addShellClass(clz.getName());
					continue;
				}
				if (clz.isAnnotationPresent(StatusClass.class))
					BusShellStatus.addStatusClass(clz);
			} catch (ClassNotFoundException e) {
				DevAssistant.errorln(e);
			}
		}
		
		// add xml of portal
		List<URL> url = ClassFinder.findXmlUrl(this.getClass().getPackage().getName(), 
				BusCommonServerActivator.getClassLoader());
		
		// add properties of portal
		ClassFinder.findResourceUrl(this.getClass().getPackage().getName(), 
				BusCommonServerActivator.getClassLoader());
	}
	
	private void destroy() {
		
	}
	
	protected abstract void beforeMount() throws CIBusException;
	
	protected abstract void afterMount() throws CIBusException;
	
	protected abstract void beforeUnmount() throws CIBusException;
	
	protected abstract void afterUnmount() throws CIBusException;
}

