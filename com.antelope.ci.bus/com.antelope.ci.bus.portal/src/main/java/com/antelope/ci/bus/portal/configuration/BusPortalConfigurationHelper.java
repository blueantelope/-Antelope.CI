// com.antelope.ci.bus.portal.BusPortalConfiguration.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.DevAssistant;
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
	
	private static final String PORTAL_XML= "portal.xml";
	private static final String PORTAL_RESOURCE = "com.antelope.ci.bus.portal.configuration.portal";
	private static Logger log;
	private Configuration configutation;
	private ResourceReader reader;
	private ClassLoader classLoader;
	private Map<String, ConfigurationPair> configPairMap;
	private BusPortalConfigurationHelper() {
		try {
			log = CommonBusActivator.getLog4j(this.getClass());
		} catch (CIBusException e) {
			
		} 
		configPairMap = new HashMap<String, ConfigurationPair>();
	}
	
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	public void init() throws CIBusException {
		parseXml();
		parseProperties();
		convert();
		initConfigurationPair();
	}
	
	private void parseXml() throws CIBusException {
		InputStream in = BusPortalConfigurationHelper.class.getResourceAsStream(PORTAL_XML);
		configutation = (Configuration) BusXmlHelper.parse(Configuration.class, in);
	}
	
	private void parseProperties() throws CIBusException {
		reader = new ResourceReader();
		if (classLoader != null) {
			reader.addResource(PORTAL_RESOURCE, classLoader);
		} else {
			reader.addResource(PORTAL_RESOURCE);
		}
	}
	
	private void convert() {
		for (TopMenu topMenu : configutation.getTopMenus().getMenuList()) {
			if (reader.getString(topMenu.getName()) != null) {
				topMenu.setValue(reader.getString(topMenu.getName()));
			}
		}
	}
	
	private void initConfigurationPair() {
		
	}

	public Configuration getConfiguration() {
		return configutation;
	}
	
	public void addPart(Part part) {
		configutation.addPart(part);
	}
	
	public void addConfigurationPair(String packagePath) {
		try {
			List<String> propsList = ClassFinder.getPropsResource(packagePath, classLoader);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	private static class ConfigurationPair {
		private String props_name;
		private String xml_name;
		public ConfigurationPair(String props_name, String xml_name) {
			super();
			this.props_name = props_name;
			this.xml_name = xml_name;
		}
		public String getProps_name() {
			return props_name;
		}
		public String getXml_name() {
			return xml_name;
		}
	}
}

