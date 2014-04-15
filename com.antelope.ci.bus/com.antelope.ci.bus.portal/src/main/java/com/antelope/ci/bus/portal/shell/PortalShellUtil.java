// com.antelope.ci.bus.portal.shell.ShellUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.shell;

import java.util.List;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.server.shell.ShellUtil;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-14		下午2:56:03 
 */
public class PortalShellUtil {
	private static final Logger log = Logger.getLogger(ShellUtil.class);
	private static final BusPortalConfigurationHelper config_helper = BusPortalConfigurationHelper.getHelper();
	
	public static String getNextStatus(String status) {
		String nextShell = getNextShell(status);
		if (nextShell == null) {			
			return status;
		} else {
			try {
				return ShellUtil.getStatus(nextShell);
			} catch (CIBusException e) {
				DevAssistant.assert_exception(e);
				return status;
			}
		}
	}
	
	public static String getNextShell(String status) {
		List<String> classNameList = config_helper.sortPortalShell();
		int n = 0;
		for (String className : classNameList) {
			n++;
			try {
				String shellStatus = ShellUtil.getStatus(className);
				if (!StringUtil.empty(shellStatus) && shellStatus.equalsIgnoreCase(status)) {
					int next = n;
					if (next == classNameList.size())		next = 0;
					return classNameList.get(next);
				}
			} catch (CIBusException e) {
				DevAssistant.assert_exception(e);
			}	
		}
		
		return null;
	}
}

