// com.antelope.ci.bus.portal.shell.ShellUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell;

import java.util.List;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.server.shell.base.BusShell;
import com.antelope.ci.bus.server.shell.base.BusShellMode;
import com.antelope.ci.bus.server.shell.base.BusShellMode.BaseShellMode;
import com.antelope.ci.bus.server.shell.base.ShellPalette;
import com.antelope.ci.bus.server.shell.base.ShellText;
import com.antelope.ci.bus.server.shell.util.ShellUtil;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-14		下午2:56:03 
 */
public class PortalShellUtil {
	public final static String LAYOUT_CONTENT = "layout.content";
	
	private final static Logger log = Logger.getLogger(ShellUtil.class);
	private final static BusPortalConfigurationHelper config_helper = BusPortalConfigurationHelper.getHelper();
	
	public static ShellPalette getContentPalette(BusShell shell) {
		return shell.getPalette(LAYOUT_CONTENT);
	}
	
	public static String getNextStatus(String status, ClassLoader classLoader) {
		String nextShell = getNextShell(status, classLoader);
		if (nextShell == null) {			
			return status;
		} else {
			try {
				return ShellUtil.getStatus(nextShell, classLoader);
			} catch (CIBusException e) {
				DevAssistant.assert_exception(e);
				return status;
			}
		}
	}
	
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
	
	public static String getNextShell(String status, ClassLoader classLoader) {
		List<String> classNameList = config_helper.sortPortalShell();
		int n = 0;
		for (String className : classNameList) {
			n++;
			try {
				String shellStatus = ShellUtil.getStatus(className, classLoader);
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
	
	public static int maxLine(List<List<String>> contentList) {
		int max = 0;
		for (List<String> content : contentList) {
			int cw = 0;
			for (String c : content) {
				if (ShellText.isShellText(c)) {
					cw += ShellText.length(c);
				} else {
					cw += StringUtil.lengthVT(c);
				}
			}
			
			max = max > cw ? max : cw;
		}

		return max;
	}
	
	public static boolean isMainMode(BusPortalShell shell) {
		return BusShellMode.isMain(shell.getActiveBlock().getMode());
	}
	
	public static boolean isInputMode(BusPortalShell shell) {
		return BusShellMode.isInput(shell.getActiveBlock().getMode());
	}
	
	public static BaseShellMode getPortalMode(BusPortalShell shell) throws CIBusException {
		return BaseShellMode.toMode(shell.getActiveBlock().getMode());
	}
}
