// com.antelope.ci.bus.portal.entrance.CommonEntrance.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.entrance;

import java.util.List;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.portal.core.configuration.PortalConfiguration;
import com.antelope.ci.bus.portal.core.shell.command.PortalCommandAdapter;
import com.antelope.ci.bus.server.BusServerCondition;
import com.antelope.ci.bus.server.shell.BusShellMode;
import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.ModeClass;
import com.antelope.ci.bus.server.shell.Shell;
import com.antelope.ci.bus.server.shell.StatusClass;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandAdapter;
import com.antelope.ci.bus.server.shell.command.CommandAdapterFactory;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-17		下午4:09:19 
 */
public abstract class CommonEntrance implements Entrance {
	private static final Logger log = Logger.getLogger(CommonEntrance.class);
	private static final CommandAdapter cmdAdapter = CommandAdapterFactory.getAdapter(PortalCommandAdapter.class.getName());
	protected BusServerCondition server_condition;
	protected ClassLoader classLoader;
	
	@Override
	public void init(Object... args) throws CIBusException {
		for (Object arg : args) {
			if (arg instanceof BusServerCondition)
				this.server_condition = (BusServerCondition) arg;
			if (arg instanceof ClassLoader)
				this.classLoader = (ClassLoader) arg;
		}
		
		if (classLoader == null)
			classLoader = Thread.currentThread().getContextClassLoader();
		
		cmdAdapter.initClassLoader(classLoader);
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
		String packageName = this.getClass().getPackage().getName();
		List<String> classList = ClassFinder.findClasspath(packageName, classLoader);
		
		for (String clsname : classList) {
			try {
				Class clz = Class.forName(clsname, false, classLoader);
				if (clz.isAnnotationPresent(Shell.class))
					if (server_condition != null)
						server_condition.addShellClass(clz.getName());
				
				if (clz.isAnnotationPresent(PortalConfiguration.class)) {
					PortalConfiguration pc = (PortalConfiguration) clz.getAnnotation(PortalConfiguration.class);
					BusPortalConfigurationHelper.getHelper().addConfigPair(clz.getName(), pc.properties(), pc.xml(), pc.validate());
				}
				
				if (clz.isAnnotationPresent(StatusClass.class))
					BusShellStatus.addStatusClass(clz);
				
				if (clz.isAnnotationPresent(ModeClass.class))
					BusShellMode.addModeClass(clz);
				
				if (clz.isAnnotationPresent(Command.class))
					cmdAdapter.loadCommand(clsname);
			} catch (ClassNotFoundException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	private void destroy() {
		
	}
	
	protected abstract void beforeMount() throws CIBusException;
	
	protected abstract void afterMount() throws CIBusException;
	
	protected abstract void beforeUnmount() throws CIBusException;
	
	protected abstract void afterUnmount() throws CIBusException;
}