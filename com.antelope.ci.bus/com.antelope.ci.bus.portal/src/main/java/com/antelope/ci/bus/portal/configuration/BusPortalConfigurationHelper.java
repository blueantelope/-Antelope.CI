// com.antelope.ci.bus.portal.BusPortalConfiguration.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.configration.CfgFileReader;
import com.antelope.ci.bus.common.configration.ResourceReader;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.osgi.CommonBusActivator;
import com.antelope.ci.bus.portal.configuration.xo.Part;
import com.antelope.ci.bus.portal.configuration.xo.Portal;


/**
 * configraiton reader for portal (include main and part)
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-14		上午11:15:09 
 */
public class BusPortalConfigurationHelper {
	private static final BusPortalConfigurationHelper helper = new BusPortalConfigurationHelper();
	
	public final static BusPortalConfigurationHelper getHelper() {
		return helper;
	}
	
	private static final String LABLE_START = "${";
	private static final String LABLE_END = "}";
	private static final String PORTAL_XML= "portal.xml";
	private static final String PORTAL_RESOURCE = "com.antelope.ci.bus.portal.configuration.portal";
	private static Logger log;
	private Portal portal;
	private ResourceReader reader;
	private ClassLoader classLoader;
	private Map<String, PortalPair> configPairMap;
	private Map<String, Portal> portalExtMap;
	private static int null_name_index;
	private boolean inited = false;
	
	private BusPortalConfigurationHelper() {
		try {
			log = CommonBusActivator.getLog4j(this.getClass());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		} 
		configPairMap = new HashMap<String, PortalPair>();
	}
	
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	public synchronized void init() throws CIBusException {
		if (!inited) {
			portal = parseXml(PORTAL_XML);
			reader = parseProperties(PORTAL_RESOURCE, classLoader);
			convert(portal, reader);
			initConfigurationPair();
			portalExtMap = new LinkedHashMap<String, Portal>();
			null_name_index = 0;
			inited = true;
		}
	}
	
	public Portal getPortalExtention(String name) {
		return portalExtMap.get(name);
	}
	
	public void addExtention(String package_path) throws CIBusException {
		Portal portalExt = parseExtention(package_path);
		synchronized(this) {
			String name = portalExt.getName();
			if (name.equals("null")) name += "_" + (null_name_index++);
			portalExtMap.put(name, portalExt);
		}
	}
	
	public Portal parseExtention(String package_path) throws CIBusException {
		ClassLoader cl = classLoader==null ? this.getClass().getClassLoader() : classLoader;
		List<URL> xmlUrls = ClassFinder.findXmlUrl(package_path, cl);
		Portal portal_ext = null;
		for (URL url : xmlUrls) {
			if (isPortalResource(url.getFile(), "/")) {
				String xml_path = "/" + package_path.replace(".", "/");
				xml_path += "/" + StringUtil.getLastName(url.getFile(), "/");
				portal_ext = parseXml(xml_path);
				break;
			}
		}
		ResourceReader reader_ext = null;
		List<String> resList = ClassFinder.getPropsResource(package_path, cl);
		for (String res : resList) {
			if (isPortalResource(res, "\\.")) {
				reader_ext = parseProperties(res, cl);
				break;
			}
		}
		
		return transfer(portal_ext, reader_ext);
	}
	
	public Portal parseExtention(InputStream xml_in, String config, boolean isResource) throws CIBusException {
		ClassLoader cl = classLoader==null ? this.getClass().getClassLoader() : classLoader;
		Portal portal_ext = (Portal) BusXmlHelper.parse(Portal.class, xml_in);
		BasicConfigrationReader reader_ext;
		if (isResource) {
			reader_ext = parseProperties(config, cl);
		} else {
			reader_ext = new CfgFileReader();
			reader_ext.addConfig(config);
		}
		
		return transfer(portal_ext, reader_ext);
	}
	
	private Portal transfer(Portal portal_ext, BasicConfigrationReader reader_ext) throws CIBusException {
		if (portal_ext != null && reader_ext != null) {
			convert(portal_ext, reader_ext);
		}
		
		if (portal_ext != null && portal != null) {
			portal_ext.setBase(portal.getBase());
			portal_ext.setLayout(portal.getLayout());
			portal_ext.setParts(portal.getParts());
			portal_ext.attachExtensions();
		}
		
		return portal_ext;
	}
		
	private boolean isPortalResource(String res, String  deco) {
		String name = StringUtil.getLastName(res,  deco);
		if (name.startsWith("portal")) return true;
		return false;
	}
	
	private Portal parseXml(String xml_path) throws CIBusException {
		InputStream in = BusPortalConfigurationHelper.class.getResourceAsStream(xml_path);
		return (Portal) BusXmlHelper.parse(Portal.class, in);
	}
	
	private ResourceReader parseProperties(String resource_path, ClassLoader cl) throws CIBusException {
		ResourceReader r = new ResourceReader();
		if (cl != null) {
			r.addConfig(resource_path, cl);
		} else {
			r.addConfig(resource_path);
		}
		
		return r;
	}
	
	private void convert(Portal p, BasicConfigrationReader r) throws CIBusException {
		List<PortalReplace> replaceList = new ArrayList<PortalReplace>();
		findReplace(replaceList, p);
		for (PortalReplace pr : replaceList) {
			String new_value = replaceLable(pr.getValue(), r);
			ProxyUtil.invoke(pr.getParent(), pr.getSetter(), new Object[]{new_value});
		}
	}
	
	private String replaceLable(String value, BasicConfigrationReader r) {
		StringBuffer buf = new StringBuffer();
		int len = value.length();
		int index = 0;
		while (index < len) {
			int start = value.indexOf(LABLE_START, index);
			if (start == -1) {
				buf.append(value.substring(index, len));
				break;
			}
			int end = value.indexOf(LABLE_END, start);
			String key = value.substring(start+2, end);
			String v = r.getString(key);
			v = (v == null) ? "" : v;
			buf.append(value.substring(index, start)).append(v);
			index = end + 1;
		}
		
		return buf.toString();
	}
	
	private void findReplace(List<PortalReplace> replaceList, Object root) throws CIBusException {
		List<Object> deepList = new ArrayList<Object>();
		for (Method method : root.getClass().getMethods()) {
			if (!method.getName().equals("getClass") && method.getName().contains("get")) {
				try {
					Object o = ProxyUtil.invokeRet(method, root);
					if (o == null)
						continue;
					if (List.class.isAssignableFrom(o.getClass())) {
						deepList.addAll((List) o);
					} else {
						deepList.add(o);
						if (o instanceof String) {
							String setter = "set" + method.getName().substring(3);
							Method setMethod = root.getClass().getMethod(setter, String.class);
							if (setMethod != null) {
								PortalReplace pr = new PortalReplace(root, setter, (String) o);
								replaceList.add(pr);
							}
						}
					}
				} catch (Exception e) {
					DevAssistant.errorln(e);
				}
			}
		}
		for (Object deep : deepList) {
			if (deep.getClass().isAnnotationPresent(XmlEntity.class))
				findReplace(replaceList, deep);
		}
	}
	
	
	private void initConfigurationPair() {
		
	}

	public Portal getPortal() {
		return portal;
	}
	
	public void addPart(Part part) throws CIBusException {
		portal.addPart(part);
	}
	
	public void addPortalPair(String packagePath) {
		try {
			List<String> propsList = ClassFinder.getPropsResource(packagePath, classLoader);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	private static class PortalReplace {
		private Object parent;
		private String setter;
		private String value;
		public PortalReplace(Object parent, String setter, String value) {
			super();
			this.parent = parent;
			this.setter = setter;
			this.value = value;
		}
		public Object getParent() {
			return parent;
		}
		public String getSetter() {
			return setter;
		}
		public String getValue() {
			return value;
		}
	}
	
	private static class PortalPair {
		private String props_name;
		private String xml_name;
		public PortalPair(String props_name, String xml_name) {
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

