// com.antelope.ci.bus.portal.core.shell.form.PortalFormContextFactory.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月7日		上午11:45:58 
 */
public class PortalFormContextFactory {
	private final static PortalFormContextFactory factory = new PortalFormContextFactory();
	public final static PortalFormContextFactory getFactory() {
		return factory;
	}
	
	
	private static final Logger log = Logger.getLogger(PortalFormContextFactory.class);
	private static final long CHECK_START = 5 * 1000;
	private static final long CHECK_PERIOD = 30 * 1000;
	private static Map<BusPortalShell, List<PortalFormContext>> formContextMap;
	private static List<BusPortalShell> shellList;
	
	private PortalFormContextFactory() {
		formContextMap = new ConcurrentHashMap<BusPortalShell, List<PortalFormContext>>();
		shellList = new Vector<BusPortalShell>();
		loopCheck();
	}
	
	private void loopCheck() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override public void run() {
				for (BusPortalShell pShell : formContextMap.keySet()) {
					if (pShell == null) {
						formContextMap.remove(pShell);
					} else {
						List<PortalFormContext> contextList = formContextMap.get(pShell);
						int size = contextList.size();
						int n = 0;
						List<Integer> removeList = new ArrayList<Integer>();
						while (n < size)	{
							if (contextList.get(n) == null)
								removeList.add(n);
							n++;
						}
						
						n = removeList.size();
						while (n > 0) contextList.remove(n--);
					}
				}
			}
		}, CHECK_START, CHECK_PERIOD);
	}
	
	public PortalFormContext getFormContext(BusPortalShell shell, String identity) {
		for (BusPortalShell pShell : formContextMap.keySet()) {
			if (pShell == shell) {
				List<PortalFormContext> contextList = formContextMap.get(shell);
				for (PortalFormContext context : contextList) {
					if (identity.startsWith(context.getName()))
						return context;
				}
			}
		}
		return null;
	}
	
	public PortalFormContext add(BusPortalShell shell, Class commandClass) {
		List<PortalFormContext> contextList = formContextMap.get(shell);
		if (contextList == null) {
			contextList = new ArrayList<PortalFormContext>();
			formContextMap.put(shell, contextList);
		}
		PortalFormContext context;
		try {
			context = new PortalFormContext(shell, commandClass);
			contextList.add(context);
			return context;
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		return null;
	}
	
	public void remove(BusPortalShell shell) {
		for (BusPortalShell pShell : formContextMap.keySet()) {
			if (pShell == shell) {
				formContextMap.remove(shell);
				break;
			}
		}
	}
}