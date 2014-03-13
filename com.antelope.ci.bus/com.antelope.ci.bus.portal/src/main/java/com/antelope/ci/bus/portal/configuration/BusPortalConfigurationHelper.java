// com.antelope.ci.bus.portal.BusPortalConfiguration.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

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
import com.antelope.ci.bus.portal.configuration.xo.Base;
import com.antelope.ci.bus.portal.configuration.xo.Content;
import com.antelope.ci.bus.portal.configuration.xo.EU_Point;
import com.antelope.ci.bus.portal.configuration.xo.EU_Position;
import com.antelope.ci.bus.portal.configuration.xo.Extension;
import com.antelope.ci.bus.portal.configuration.xo.Extensions;
import com.antelope.ci.bus.portal.configuration.xo.Layout;
import com.antelope.ci.bus.portal.configuration.xo.Margin;
import com.antelope.ci.bus.portal.configuration.xo.Part;
import com.antelope.ci.bus.portal.configuration.xo.Parts;
import com.antelope.ci.bus.portal.configuration.xo.Place;
import com.antelope.ci.bus.portal.configuration.xo.PlacePart;
import com.antelope.ci.bus.portal.configuration.xo.PlaceParts;
import com.antelope.ci.bus.portal.configuration.xo.Portal;
import com.antelope.ci.bus.portal.configuration.xo.RenderDelimiter;

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
	
	private static final String CP_SUFFIX 							= "classpath:";
	private static final String FILE_SUFFIX 						= "file:";
	private static final String LABLE_START = "${";
	private static final String LABLE_END = "}";
	private static final String PORTAL_XML= "/com/antelope/ci/bus/portal/configuration/portal.xml";
	private static final String PORTAL_RESOURCE = "com.antelope.ci.bus.portal.configuration.portal";
	private static Logger log;
	private Portal portal;
	private Portal usablePortal;					// 配置文件转换后
	private ResourceReader reader;
	private ClassLoader classLoader;
	private Map<String, PortalPair> configPairMap;
	private Map<String, Portal> portalExtMap;
	private static int null_name_index;
	private boolean inited = false;
	private EU_ParseType parseType;
	private final static String PARSETYPE_KEY								= "bus.portal.parse";	
	private final static String DEFAULT_PARSETYPEVALUE				= "static";
	private final static EU_ParseType DEFAULT_PARSETYPE 			= EU_ParseType.STATICAL;
	
	private BusPortalConfigurationHelper() {
		try {
			log = CommonBusActivator.getLog4j(this.getClass());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		} 
		try {
			String parTypeValue = CommonBusActivator.getStringProp(PARSETYPE_KEY, DEFAULT_PARSETYPEVALUE);
			parseType = EU_ParseType.toType(parTypeValue);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
			parseType = DEFAULT_PARSETYPE;
		} 
		configPairMap = new ConcurrentHashMap<String, PortalPair>();
		classLoader = this.getClass().getClassLoader();
		null_name_index = 0;
		portalExtMap = new ConcurrentHashMap<String, Portal>();
	}
	
	public void addConfigPair(String name, String props_name, String xml_name) {
		PortalPair pair = new PortalPair(props_name, xml_name);
		configPairMap.put(name, pair);
	}
	
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	public Portal parse(String shellClass) throws CIBusException {
		switch (parseType) {
			case DYNAMICAL:
				return parseDynamic(shellClass);
			case STATICAL:
			default:
				return parseStatic(shellClass);
		}
	}
	
	private Portal parseDynamic(String shellClass) throws CIBusException {
		Portal global_portal = parsePortal(PORTAL_XML);
		ResourceReader global_reader = parseProperties(PORTAL_RESOURCE, classLoader);
		
		return global_portal;
	}
	
	private Portal parseStatic(String shellClass) throws CIBusException {
		for (String ck : configPairMap.keySet()) {
			if (ck.equals(shellClass) || !portalExtMap.containsKey(ck)) continue;
			try {
				InputStream xml_in = getXmlStream(configPairMap.get(ck).getXml_name());
				portalExtMap.put(ck, parsePortal(xml_in));
			} catch (Exception e) {
				DevAssistant.assert_exception(e);
			}
		}
		List<String> sortList = sortPortalExtension(portalExtMap);
		
		Portal majorExt = portalExtMap.get(shellClass);
		if (majorExt != null) {
			majorExt = extend(majorExt);
		} else {
			majorExt = usablePortal;
		}
		
		convert(majorExt, reader);
		convert()
		
		Map<String, PlacePart> renderMap = majorExt.getPalcePartRenderMap();
		for (String rname : renderMap.keySet()) {
			PlacePart pp =  renderMap.get(rname);
			RenderDelimiter delimiter = pp.getRender().getDelimiter();
			String del_value = " ";
			EU_Position del_position = EU_Position.MIDDLE;
			Margin del_margin = null;
			if (delimiter != null) {
				del_value = delimiter.getValue();
				del_position = delimiter.getEU_Position();
				del_margin = delimiter.getMarginObject();
			}
			
			Part major_part = majorExt.getPart(rname);
			if (major_part == null) {
				major_part = new Part();
				major_part.setName(rname);
				Content content = new Content();
				content.setValue("");
				major_part.setContent(content);
				majorExt.addPart(major_part);
			}
			
			if (del_position == EU_Position.START)
				major_part.addForeValue(del_value);
			
			for (String extName : sortList) {
				Portal ext = portalExtMap.get(extName);
				if (ext.getPart(rname))
				if (del_position == EU_Position.MIDDLE)
					major_part.addAfterValue(del_value + ext.getPart(partName));
			}
		}
		
		return majorExt;
		
		/*
		PortalPair pp = configPairMap.get(shellClass);
		if (pp == null)
			return usablePortal;
		boolean isResource = pp.getProps_name().startsWith(CP_SUFFIX) ? true : false;
		String a_xml = trunckConfig(pp.getXml_name() + ".xml");
		String a_pro = trunckConfig(pp.getProps_name());
		try {
			Class thisCls = Class.forName(shellClass, false, classLoader);
			String package_name = thisCls.getPackage().getName();
			return parseExtention(thisCls.getResourceAsStream(a_xml), a_pro, isResource, package_name);
		} catch (ClassNotFoundException e) {
			return usablePortal;
		}
		*/
	}
	
	private List<String> sortPortalExtension(Map<String, Portal> extMap) {
		List<String> resutlList = new ArrayList<String>();
		Map<String, Integer> sortMap = new TreeMap<String, Integer>();
		List<String> unsortList = new ArrayList<String>();
		for (String ck : extMap.keySet()) {
			Portal ext = extMap.get(ck);
			if (ext.getBase() != null) {
				if (ext.getBase().getOrder() != 0) {
					sortMap.put(ck, ext.getBase().getOrder());
					continue;
				}
			}
			unsortList.add(ck);
		}

		List<Map.Entry<String, Integer>> sortList = new ArrayList<Map.Entry<String, Integer>>(sortMap.entrySet());
		Collections.sort(sortList, new Comparator<Map.Entry<String,Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o1.getValue() - o2.getValue();
			}
		});
		
		for (Map.Entry<String, Integer> entry : sortList)
			resutlList.add(entry.getKey());
		resutlList.addAll(unsortList);
		return resutlList;
	}
	
	public synchronized void init() throws CIBusException {
		if (!inited && parseType == EU_ParseType.STATICAL) {
			portal = parsePortal(PORTAL_XML);
			reader = parseProperties(PORTAL_RESOURCE, classLoader);
			usablePortal = portal.clonePortal();
			convert(usablePortal, reader);
			inited = true;
		}
	}
	
	public Portal getPortalExtention(String name) {
		return portalExtMap.get(name);
	}
	
	private void addExtention(String name, Portal portalExt) {
		for (String key : portalExtMap.keySet())
			if (name.equalsIgnoreCase(key))
				return;
		portalExtMap.put(name, portalExt);
	}
	
	@Deprecated
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
				portal_ext = parsePortal(xml_path);
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
	
	public Portal parseExtention(InputStream xml_in, String config, boolean isResource, String packageName) throws CIBusException {
		ClassLoader cl = classLoader==null ? this.getClass().getClassLoader() : classLoader;
		Portal portal_ext = (Portal) BusXmlHelper.parse(Portal.class, xml_in);
		BasicConfigrationReader reader_ext;
		if (isResource) {
			try {
				reader_ext = parseProperties(config, cl);
			} catch (CIBusException e) {
				reader_ext = parseProperties(packageName+"."+config, cl);
			}
		} else {
			reader_ext = new CfgFileReader();
			reader_ext.addConfig(config);
		}
		
		return transfer(portal_ext, reader_ext);
	}
	
	private InputStream getXmlStream(String xpath) throws Exception {
		if (!StringUtil.endsWithIgnoreCase(xpath, ".xml"))
			xpath += ".xml";
		if (xpath.startsWith(CP_SUFFIX)) {
			String n_xpath = xpath.substring(CP_SUFFIX.length());
			return BusPortalConfigurationHelper.class.getResourceAsStream(n_xpath);
		}
		
		if (xpath.startsWith(FILE_SUFFIX)) {
			String n_xpath = xpath.substring(FILE_SUFFIX.length());
			return new FileInputStream(n_xpath);
		}
		
		return new URL(xpath).openStream();
	}
	
	private String trunckConfig(String config) {
		String new_config = config;
		if (config.startsWith(CP_SUFFIX))
			new_config = config.substring(CP_SUFFIX.length());
		
		if (new_config.startsWith(FILE_SUFFIX))
			new_config = config.substring(FILE_SUFFIX.length());
		
		return new_config;
	}
	
	private Portal transfer(Portal portal_ext, BasicConfigrationReader reader_ext) throws CIBusException {
		if (portal_ext != null && reader_ext != null)
			convert(portal_ext, reader_ext);
		
		if (portal_ext != null)
			portal_ext = extend(portal_ext);
		
		return portal_ext;
	}
		
	private boolean isPortalResource(String res, String  deco) {
		String name = StringUtil.getLastName(res,  deco);
		if (name.startsWith("portal")) return true;
		return false;
	}
	
	private Portal parsePortal(String xml_path) throws CIBusException {
		InputStream in = BusPortalConfigurationHelper.class.getResourceAsStream(xml_path);
		return (Portal) BusXmlHelper.parse(Portal.class, in);
	}

	private Portal parsePortal(InputStream xml_in) throws CIBusException {
		return (Portal) BusXmlHelper.parse(Portal.class, xml_in);
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
	
	private Portal exend(Portal globalPortal, Portal extPortal) throws CIBusException {
		Portal new_portal = extPortal;
		try {
			new_portal = globalPortal.clonePortal();
			Extensions exts = extPortal.getExtensions();
			new_portal.getPlacePartMap(	);
			if (exts != null) {
				List<Extension> extList = exts.getExtentionList();
				if (extList != null) {
					for (Extension ext : extList) {
						EU_Point eup = ext.getPoint();
						switch (eup) {
							case BASE:
								extendBase(ext, new_portal);
								break;
							case LAYOUT:
								extendLayout(ext, new_portal);
								break;
							case PARTS:
								extendParts(ext, new_portal);
								break;
						}
					}
				}
			}
		} catch (Exception e) {
			DevAssistant.errorln(e);
		}
		
		return new_portal;
	}
	
	private Portal extend(Portal extPortal) throws CIBusException {
		return exend(portal, extPortal);
	}
	
	
	private void extendBase(Extension ext, Portal new_portal) throws CIBusException {
		Base extBase = ext.getBase();
		if (extBase == null) 
			return;
		
		Base base = new_portal.getBase();
		if (base == null) {
			base = extBase;
		} else {
			switch (extBase.getEmbed()) {
				case REPLACE:
					base = extBase;
					break;
				case APPEND:
					List<PortalReplace> replaceList = new ArrayList<PortalReplace>();
					findReplace(replaceList, base);
					for (PortalReplace pr : replaceList) {
						try {
							ProxyUtil.invoke(base, pr.getSetter(), new Object[]{pr.getValue()});
						} catch (CIBusException e) {
							DevAssistant.assert_exception(e);
						}
					}
					break;
			}
		}
		
		new_portal.setBase(base);
	}
	
	private void extendLayout(Extension ext, Portal new_portal) throws CIBusException {
		List<PlaceParts> partsList = ext.getPlacePartList();
		if (partsList == null) return;
		Collections.sort(partsList, new Comparator<PlaceParts>() {
			@Override
			public int compare(PlaceParts p1, PlaceParts p2) {
				String[] p1s = p1.getName().split(".");
				String[] p2s = p2.getName().split(".");
				return p1s.length - p2s.length;
			}
			
		});
		Map<String, PlaceParts> ppsMap = new_portal.makePlacePartsMap();
		Layout layout = new_portal.getLayout();
		if (layout == null) {
			Layout n_layout= new Layout();
			for (PlaceParts parts : partsList) {
				String pname = parts.getName();
				PlaceParts pps = ppsMap.get(pname);
				switch (parts.getEmbed()) {
					case REPLACE:
						if (pps != null)
							layout.appendPlaceParts(pname, parts);
						break;
					case APPEND:
						if (pps != null) {
							layout.appendPlaceParts(pname, parts);
						} else {
							int index = pname.lastIndexOf(".");
							if (index == -1) break;
							String nameKey = pname.substring(0, index);
							String addPname = pname.substring(index);
							List<String> pnameList = new ArrayList();
							PlaceParts find_pps = null;
							for (; index != -1; 
									index=nameKey.lastIndexOf("."), nameKey = nameKey.substring(0, index), addPname = nameKey.substring(index)) {
								find_pps = ppsMap.get(nameKey);
								if (find_pps != null)
									break;
								else
									pnameList.add(addPname);
							}
							if (find_pps == null) {
								int pIndex = pnameList.size();
								boolean added = false;
								Place add_place = null;
								PlaceParts add_pparts = null;
								while (pIndex > 0) {
									String add_pname = pnameList.get(--pIndex);
									if (pIndex !=0 && added) {
										add_pparts = new PlaceParts();
										add_pparts.addPlace(add_place);
									}
									add_place = new Place();
									add_place.setName(add_pname);
									if (!added) {
										add_place.setParts(parts);
										added = true;
									} else {
										add_place.setParts(add_pparts);
									}
									if (pIndex == 0) {
										layout.addPlace(add_place);
									}
								}
							} else {
								find_pps.appendParts(parts);
							}
							if (find_pps != null)
								layout.appendPlaceParts(pname, find_pps);
						}
						break;
				}
				
			}
			new_portal.setLayout(n_layout);
		}
	}
	
	private void extendParts(Extension ext, Portal new_portal) {
		Parts newParts = new_portal.getParts();
		for (Part part : ext.getPartList()) {
			switch (part.getEmbed()) {
				case REPLACE:
					newParts.addPart(part);
					break;
				case APPEND:
					newParts.addPart(part);
					break;
			}
		}
	}
	
	private void convert(Portal p, BasicConfigrationReader r) throws CIBusException {
		List<PortalReplace> replaceList = new ArrayList<PortalReplace>();
		findReplace(replaceList, p);
		for (PortalReplace pr : replaceList) {
			String new_value = replaceLable((String) pr.getValue(), r);
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
	
	private void findReplace(List<PortalReplace> replaceList, Object root, Class... replaceClasses) throws CIBusException {
		List<Object> deepList = new ArrayList<Object>();
		for (Method method : root.getClass().getMethods()) {
			if (method.getName().startsWith("get") && !method.getName().startsWith("getClass")) {
				try {
					Object o = ProxyUtil.invokeRet(method, root);
					if (o == null)
						continue;
					if (List.class.isAssignableFrom(o.getClass())) {
						deepList.addAll((List) o);
					} else {
						deepList.add(o);
						if (replaceClasses.length == 0) {
							addReplaceEntry(replaceList, root, method, o);
							continue;
						}
						for (Class c : replaceClasses) {
							if (c.isInstance(o)) {
								addReplaceEntry(replaceList, root, method, o);
								break;
							}
						}
					}
				} catch (Exception e) {
					DevAssistant.assert_exception(e);
				}
			}
		}
		for (Object deep : deepList) {
			if (deep.getClass().isAnnotationPresent(XmlEntity.class))
				findReplace(replaceList, deep);
		}
	}
	
	private void addReplaceEntry(List<PortalReplace> replaceList, Object root, Method method, Object o) throws CIBusException {
		try {
			String setter = "set" + method.getName().substring(3);
			Method setMethod = root.getClass().getMethod(setter, String.class);
			if (setMethod != null) {
				PortalReplace pr = new PortalReplace(root, setter, o);
				replaceList.add(pr);
			}
		} catch (Exception e) {
			new CIBusException("", e);
		}
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
		private Object value;
		public PortalReplace(Object parent, String setter, Object value) {
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
		public Object getValue() {
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

