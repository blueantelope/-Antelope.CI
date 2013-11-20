// com.antelope.ci.bus.portal.BusPortalConfiguration.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.configration.ResourceReader;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;
import com.antelope.ci.bus.osgi.CommonBusActivator;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-14		上午11:15:09 
 */
public class BusPortalConfigurationHelper {
	private static final BusPortalConfigurationHelper helper = new BusPortalConfigurationHelper();
	
	public final static BusPortalConfigurationHelper getHelper() {
		return helper;
	}
	
	private static final String PORTAL_TERMINAL_XML= "portal_terminal.xml";
	private static final String PORTAL_TERMINAL_RESOURCE = "com.antelope.ci.bus.portal.portal_terminal";
	private static Logger log;
	private Configuration configutation;
	private ResourceReader reader; 
	private BusPortalConfigurationHelper() {
		try {
			log = CommonBusActivator.getLog4j(this.getClass());
		} catch (CIBusException e) {
			e.printStackTrace();
		} 
		try {
			init();
		} catch (CIBusException e) {
			
		}
	}
	
	private void init() throws CIBusException {
		parseXml();
		parseProperties();
		convert();
	}
	
	public void parseXml() throws CIBusException {
		InputStream in = BusPortalConfigurationHelper.class.getResourceAsStream(PORTAL_TERMINAL_XML);
		configutation = (Configuration) BusXmlHelper.parse(Configuration.class, in);
	}
	
	public Configuration getConfiguration() {
		return configutation;
	}
	
	private void parseProperties() throws CIBusException {
		reader = new ResourceReader();
		reader.addResource(PORTAL_TERMINAL_RESOURCE);
	}
	
	private void convert() {
		for (TopMenu topMenu : configutation.getTopMenus().getMenuList()) {
			if (reader.getString(topMenu.getName()) != null) {
				topMenu.setValue(reader.getString(topMenu.getName()));
			}
		}
	}

}

