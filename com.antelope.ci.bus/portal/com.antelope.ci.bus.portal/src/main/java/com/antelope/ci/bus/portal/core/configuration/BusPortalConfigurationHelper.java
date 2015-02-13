// com.antelope.ci.bus.portal.BusPortalConfiguration.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.Constants;
import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.ResourceUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.configration.CfgFileReader;
import com.antelope.ci.bus.common.configration.ResourceReader;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.osgi.BusActivator;
import com.antelope.ci.bus.portal.BusPortalActivator;
import com.antelope.ci.bus.portal.core.configuration.xo.Portal;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_ORIGIN;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Point;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Position;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.Margin;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Action;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Base;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Contents;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Extension;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Extensions;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Layout;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Part;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Parts;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Place;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.PlacePart;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.PlaceParts;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.RenderDelimiter;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.RenderFont;

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
	
	private static final String PORTAL_XSD = "/com/antelope/ci/bus/portal/core/configuration/portal.xsd";
	private static final String PORTAL_XML = "/com/antelope/ci/bus/portal/core/configuration/portal.xml";
	private static final String PORTAL_RESOURCE = "com.antelope.ci.bus.portal.core.configuration.portal";
	private static Logger log;
	private Portal portal;
	private Portal usablePortal;					// 配置文件转换后
	private ResourceReader reader;
	private ClassLoader classLoader;
	private Map<String, PortalPair> configPairMap;
	private Map<String, Portal> portalExtMap;
	private static int null_name_index;
	private boolean inited = false;
	private PARSER_TYPE parserType;
	private static InputStream xsd_in = null;
	private final static PARSER_TYPE DEF_PARSER_TYPE = PARSER_TYPE.STATICAL;
	
	private BusPortalConfigurationHelper() {
		try {
			log = BusActivator.getLog4j(this.getClass());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		} 
		try {
			String parserValue = BusPortalActivator.getParser();
			parserType = PARSER_TYPE.toType(parserValue);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
			parserType = DEF_PARSER_TYPE;
		} 
		configPairMap = new ConcurrentHashMap<String, PortalPair>();
		classLoader = this.getClass().getClassLoader();
		null_name_index = 0;
		portalExtMap = new ConcurrentHashMap<String, Portal>();
		xsd_in = BusPortalConfigurationHelper.class.getResourceAsStream(PORTAL_XSD);
	}
	
	public void addConfigPair(String name, String props_name, String xml_name, boolean validate) {
		PortalPair pair = new PortalPair(props_name, xml_name, validate);
		configPairMap.put(name, pair);
	}
	
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	public Portal parse(String shellClass) throws CIBusException {
		switch (parserType) {
			case DYNAMICAL:
				return parseDynamic(shellClass);
			case STATICAL:
			default:
				return parseStatic(shellClass);
		}
	}
	
	private Portal parseDynamic(String shellClass) throws CIBusException {
		PortalPair portalPair = configPairMap.get(shellClass);
		Portal global_portal;
		if (portalPair != null) {
			if (portalPair.isValidate())
				global_portal = validateAndParsePortal(PORTAL_XML);
			else
				throw new CIBusException("", "validate error");
		}
		
		global_portal = parsePortal(PORTAL_XML);
		ResourceReader global_reader = parseResource(PORTAL_RESOURCE, classLoader);
		
		return global_portal;
	}
	
	private Portal parseStatic(String shellClass) throws CIBusException {
		Map<String, Portal> minorPortalMap = new HashMap<String, Portal>();
		Portal majorExt = null;
		BasicConfigrationReader majorExtReader = null;
		PortalPair portalPair = configPairMap.get(shellClass);
		for (String configPairName : configPairMap.keySet()) {
			try {
				InputStream xml_in = getXmlStream(configPairMap.get(configPairName).getXml_name());
				Portal extPortal;
				if (configPairName.equals(shellClass)) {
					if (portalPair != null && portalPair.isValidate())
						majorExt = validateAndParsePortal(xml_in);
					else 
						majorExt = parsePortal(xml_in);
					majorExtReader = parseProperties(configPairMap.get(configPairName).getProps_name(), configPairName, classLoader);
					extPortal = majorExt;
				} else {
					if (portalPair != null && portalPair.isValidate())
						extPortal = validateAndParsePortal(xml_in);
					else 
						extPortal = parsePortal(xml_in);
					minorPortalMap.put(configPairName, extPortal);
				}
				portalExtMap.put(configPairName, extPortal);
			} catch (Exception e) {
				DevAssistant.assert_exception(e);
			}
		}
		List<String> sortList = sortPortalMap(minorPortalMap);
		
		if (majorExt != null) {
			majorExt = extend(majorExt);
		} else {
			majorExt = usablePortal;
		}
		
		List<PortalReplace> replaceList = genPortalReplaceList(majorExt);
		for (PortalReplace portalReplace : replaceList) {
			String new_value = "";
			switch (portalReplace.getOrigin()) {
				case GLOBAL:
					new_value = (String) portalReplace.getValue();
					if (ResourceUtil.needReplace(new_value))
						new_value = ResourceUtil.replaceLableForReader(new_value, reader);
					break;
				case PART:
					if (majorExtReader != null)
						new_value = ResourceUtil.replaceLableForReader((String) portalReplace.getValue(), majorExtReader);
					break;
			}
			if (!StringUtil.empty(new_value))
				ProxyUtil.invoke(portalReplace.getParent(), portalReplace.getSetter(), new Object[]{new_value});
		}
		
		Map<String, PlacePart> renderMap = majorExt.getPalcePartRenderMap();
		for (String renderName : renderMap.keySet()) {
			PlacePart placePart =  renderMap.get(renderName);
			RenderDelimiter delimiter = placePart.getRender().getDelimiter();
			RenderFont ext_font = placePart.getRender().getFont();
			RenderFont hit_font = majorExt.getSwitchHitFont();
			if (hit_font == null)
				hit_font = ext_font;
			String delimiter_value = " ";
			EU_Position delimiter_position = EU_Position.MIDDLE;
			Margin delimiter_margin = null;
			if (delimiter != null) {
				delimiter_value = delimiter.getValue();
				delimiter_position = delimiter.getEU_Position();
				delimiter_margin = delimiter.getMarginObject();
			}
			
			Part majorPart = majorExt.getPart(placePart.getName());
			if (majorPart == null) {
				majorPart = new Part();
				majorPart.setName(renderName);
				majorExt.addPart(majorPart);
			}
			majorPart.addContentsFont(hit_font);
			
			List<Contents> newContentsList = new ArrayList<Contents>();
			if (delimiter_position == EU_Position.START)
				newContentsList.addAll(Part.createStartContentsList(delimiter_value, delimiter_margin));
			
			List<PortalclassPart> portalClassPartList =  new ArrayList<PortalclassPart>();
			int major_order = majorExt.getOrder();
			boolean added = false;
			for (String portalClass : sortList) {
				Portal extPortal = minorPortalMap.get(portalClass);
				int ext_order = extPortal.getOrder();
				if (!added && major_order != -1 && major_order <= ext_order) {
					portalClassPartList.add(new PortalclassPart("", majorPart));
					added = true;
				}
				Part extPart = extPortal.getExtPart(placePart.getName());
				if (extPart != null)
					portalClassPartList.add(new PortalclassPart(portalClass, extPart));
			}
			if (!added)
				portalClassPartList.add(new PortalclassPart("", majorPart));
			
			Collections.sort(portalClassPartList, new Comparator<PortalclassPart>() {
				@Override
				public int compare(PortalclassPart p1, PortalclassPart p2) {
					return p1.getPart().getSort() - p2.getPart().getSort();
				}
			});
		
			int extList_count = 0;
			EU_Position position = EU_Position.START;
			if (portalClassPartList.size() > 1) {
				for (PortalclassPart portalclassPart : portalClassPartList) {
					if ((++extList_count) == portalClassPartList.size())
						position = EU_Position.END;
					String portalclass = portalclassPart.getPortalClass();
					if (StringUtil.empty(portalclass)) {
						newContentsList.addAll(Part.createContensList(majorPart, "", delimiter_value, delimiter_margin, position));
					} else {
						Part extPart = portalclassPart.getPart();
						BasicConfigrationReader[] readerList = new BasicConfigrationReader[] {
								parseProperties(configPairMap.get(portalclass).getProps_name(), portalclass, classLoader),
								reader
						};
						extPart.replace(readerList);
						newContentsList.addAll(Part.createContensList(extPart, "", delimiter_value, delimiter_margin, position));
					}
					majorPart.resetContentsList(newContentsList, position);
					if (position == EU_Position.START)
						position = EU_Position.MIDDLE;
				}
			}	
			if (delimiter_position == EU_Position.END)
				newContentsList.addAll(Part.createEndContentsList(delimiter_value, delimiter_margin));
			majorPart.setAllContentsList(newContentsList);
		}
		
		return majorExt;
	}
	
	private static class PortalclassPart {
		private String portalClass;
		private Part part;
		public PortalclassPart(String portalClass, Part part) {
			this.portalClass = portalClass;
			this.part = part;
		}
		public String getPortalClass() {
			return portalClass;
		}
		public Part getPart() {
			return part;
		}
	}
	
	private List<String> sortPortalMap(Map<String, Portal> portalMap) {
		List<String> resutlList = new ArrayList<String>();
		Map<String, Integer> sortMap = new TreeMap<String, Integer>();
		List<String> unsortList = new ArrayList<String>();
		for (String ck : portalMap.keySet()) {
			Portal ext = portalMap.get(ck);
			if (ext.getOrder() != -1) {
				sortMap.put(ck, ext.getOrder());
				continue;
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
	
	public List<String> sortPortalShell() {
		return sortPortalMap(portalExtMap);
	}
	
	public synchronized void init() throws CIBusException {
		if (!inited && parserType == PARSER_TYPE.STATICAL) {
			portal = parsePortal(PORTAL_XML);
			reader = parseResource(PORTAL_RESOURCE, classLoader);
			usablePortal = portal.clonePortal();
			convert(usablePortal, reader);
			inited = true;
		}
	}
	
	public Map<String, Portal> getPortalExtMap() {
		return portalExtMap;
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
				reader_ext = parseResource(res, cl);
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
				reader_ext = parseResource(config, cl);
			} catch (CIBusException e) {
				reader_ext = parseResource(packageName+"."+config, cl);
			}
		} else {
			reader_ext = new CfgFileReader();
			reader_ext.addConfig(config);
		}
		
		return transfer(portal_ext, reader_ext);
	}
	
	private InputStream getXmlStream(String xpath) throws Exception {
		return ResourceUtil.getXmlStream(BusPortalConfigurationHelper.class, xpath);
	}
	
	private String trunckConfig(String config) {
		String new_config = config;
		if (config.startsWith(Constants.CP_SUFFIX))
			new_config = config.substring(Constants.CP_SUFFIX.length());
		
		if (new_config.startsWith(Constants.FILE_SUFFIX))
			new_config = config.substring(Constants.FILE_SUFFIX.length());
		
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
	
	private Portal validateAndParsePortal(String xml_path) throws CIBusException {
		InputStream in = BusPortalConfigurationHelper.class.getResourceAsStream(xml_path);
		return (Portal) BusXmlHelper.parse(Portal.class, in, xsd_in);
	}

	private Portal parsePortal(InputStream xml_in) throws CIBusException {
		return (Portal) BusXmlHelper.parse(Portal.class, xml_in);
	}
	
	private Portal validateAndParsePortal(InputStream xml_in) throws CIBusException {
		return (Portal) BusXmlHelper.parse(Portal.class, xml_in, xsd_in);
	}
	
	private BasicConfigrationReader parseProperties(String path, String packageName, ClassLoader cl) throws CIBusException {
		String rPath = trunckConfig(path);
		BasicConfigrationReader r;
		if (path.contains("classpath:")) {
			try {
				r = parseResource(rPath, cl);
			} catch (CIBusException e) {
				r = parseResource(packageName+"."+path, cl);
			}
		} else {
			r = new CfgFileReader();
			r.addConfig(path);
		}
		
		return r;
	}
	
	private ResourceReader parseResource(String resource_path, ClassLoader cl) throws CIBusException {
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
			Extensions extensions = extPortal.getExtensions();
			new_portal.getPlacePartMap(	);
			if (extensions != null) {
				List<Extension> extensionList = extensions.getExtentionList();
				if (extensionList != null) {
					for (Extension extension : extensionList) {
						EU_Point eup = extension.toPoint();
						switch (eup) {
							case BASE:
								extendBase(extension, new_portal);
								break;
							case ACTION:
								extendAction(extension, new_portal);
								break;
							case LAYOUT:
								extendLayout(extension, new_portal);
								break;
							case PARTS:
								extendParts(extension, new_portal);
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
	
	
	private void extendBase(Extension extension, Portal new_portal) throws CIBusException {
		Base extBase = extension.getBase();
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
	
	private void extendAction(Extension extension, Portal new_portal) throws CIBusException {
		Action extAction = extension.getAction();
		if (extAction != null)
			extAction.deweight();
		
		Action action = new_portal.getAction();
		if (action == null) {
			new_portal.setAction(extAction);
		} else {
			action.deweight();
			if (extAction != null)
				action.merge(extAction);
		}
	}
	
	private void extendLayout(Extension extension, Portal new_portal) throws CIBusException {
		List<PlaceParts> partsList = extension.getPlacePartList();
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
	
	private void extendParts(Extension extension, Portal new_portal) {
		Parts newParts = new_portal.getParts();
		for (Part part : extension.getPartList()) {
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
			String new_value = (String) pr.getValue();
			if (ResourceUtil.needReplace(new_value))
				new_value = ResourceUtil.replaceLableForReader(new_value, r);
			ProxyUtil.invoke(pr.getParent(), pr.getSetter(), new Object[]{new_value});
		}
	}
	
	private List<PortalReplace> genPortalReplaceList(Portal root) {
		List<PortalReplace> replaceList = new ArrayList<PortalReplace>();
		PortalReplaceTree<PortalReplace, PortalReplaceTree> tree = genPortalReplaceTree(root);
		genPortalReplaceList(replaceList, tree);
		return replaceList;
	}
	
	private void genPortalReplaceList(List<PortalReplace> replaceList, PortalReplaceTree<PortalReplace, PortalReplaceTree> tree) {
		if (tree.hasValue()) {
			PortalReplace pr = (PortalReplace) tree.getValue();
			boolean added = true;
			for (PortalReplace existPr : replaceList) {
				if (pr.exist(existPr)) {
					added = false;
					break;
				}
			}
			if (added)
				replaceList.add(pr);
		}
		
		for (Object child : tree.getChildren())
			genPortalReplaceList(replaceList, (PortalReplaceTree) child);
	}
	
	private PortalReplaceTree genPortalReplaceTree(Portal root) {
		PortalReplaceTree<PortalReplace, PortalReplaceTree> tree = new PortalReplaceTree<PortalReplace, PortalReplaceTree>();
		tree.isRoot();
		genPortalReplaceTree(root, tree, root, EU_ORIGIN.GLOBAL);
		return tree;
	}
	
	private void genPortalReplaceTree(Portal replacePortal, PortalReplaceTree tree, Object root, EU_ORIGIN origin) {
		List<PortalReplaceTree<PortalReplace, PortalReplaceTree>> deepList = new ArrayList<PortalReplaceTree<PortalReplace, PortalReplaceTree>>();
		EU_ORIGIN child_origin = origin;
		
		if (root instanceof Part) {
			Part p = (Part) root;
			PlacePart placePart = replacePortal.getPlacePart(p.getName());
			if (placePart != null) {
				try {
					child_origin =  EU_ORIGIN.toOrigin(placePart.getOrigin());
				} catch (CIBusException e) { }
			}
		} else {
			try {
				Method originMethod = null;
				try {
					originMethod = root.getClass().getMethod("getOrigin");
				} catch (Exception e) {}
				
				if (originMethod != null) {
					String origin_str = "";
					try {
						origin_str = (String) ProxyUtil.invokeRet(root, "getOrigin");
						child_origin = EU_ORIGIN.toOrigin(origin_str);
					} catch (CIBusException e) { }
				}
			} catch (Exception e) {
				DevAssistant.assert_exception(e);
			}
		}
		
		for (Method method : root.getClass().getMethods()) {
			if (method.getName().startsWith("get") 
					&& !method.getName().startsWith("getClass") 
					&& !method.getName().startsWith("getOrigin") 
					&& !ProxyUtil.hasArguments(method)) {
				try {
					Object o = ProxyUtil.invokeRet(root, method);
					if (o == null)
						continue;
					if (List.class.isAssignableFrom(o.getClass())) {
						PortalReplace child_pr = new PortalReplace(root, "", o, child_origin);
						PortalReplaceTree child = new PortalReplaceTree();
						child.setValue(child_pr);
						child.isList();
						tree.addChild(child);
						deepList.add(child);
					} else {
						String setter = "set" + method.getName().substring(3);
						Method setMethod = null;
						for (Method m : root.getClass().getMethods()) {
							if (m.getName().equals(setter)) {
								setMethod = m;
								break;
							}
						}
						if (setMethod != null) {
							boolean isStringValue = method.getReturnType() == String.class;
							PortalReplace child_pr = new PortalReplace(root, setter, o, child_origin);
							PortalReplaceTree child = new PortalReplaceTree();
							child.setValue(child_pr);
							if (isStringValue)
								child.isStringValue();
							tree.addChild(child);
							deepList.add(child);
						}
					}
				} catch (Exception e) {
					DevAssistant.assert_exception(e);
				}
			}
		}
		for (PortalReplaceTree deep : deepList) {
			if (deep.isList) {
				List list =(List) deep.getValue().getValue();
				for (Object o : list)
					if (deep.getValue() != null)
						genPortalReplaceTree(replacePortal, deep, o, ((PortalReplace) deep.getValue()).getOrigin());
			} else {
				if (deep.getValue() != null)
					genPortalReplaceTree(replacePortal, deep, deep.getValue().getValue(), ((PortalReplace) deep.getValue()).getOrigin());
			}
		}
	}
	
	private void findReplace(List<PortalReplace> replaceList, Object root, Class... replaceClasses) throws CIBusException {
		List<Object> deepList = new ArrayList<Object>();
		
		for (Method method : root.getClass().getMethods()) {
			if (method.getName().startsWith("get") 
					&& !method.getName().startsWith("getClass") 
					&& !ProxyUtil.hasArguments(method)) {
				try {
					Object o = ProxyUtil.invokeRet(root, method);
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
				String orgin = "";
				try {
					orgin = (String) ProxyUtil.invokeRet(root, "getOrigin");
				} catch (CIBusException e) {
					orgin = EU_ORIGIN.GLOBAL.getName();
				}
				if (!StringUtil.empty(orgin) && orgin.equalsIgnoreCase(EU_ORIGIN.PART.getName()))
					pr.setPartForOrigin();
					
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
	
	private static class PortalReplaceTree<R extends PortalReplace, Tree extends PortalReplaceTree> extends XOReplaceTree {
		public PortalReplaceTree() {
			super();
		}
	}
	
	private static class PortalReplace extends XOReplace {
		private EU_ORIGIN origin;
		
		public PortalReplace(Object parent, String setter, Object value) {
			super(parent, setter, value);
			this.origin = EU_ORIGIN.GLOBAL;
		}
		public PortalReplace(Object parent, String setter, Object value, EU_ORIGIN origin) {
			super(parent, setter, value);
			this.origin = origin;
		}
		
		public EU_ORIGIN getOrigin() {
			return origin;
		}
		public void setOrigin(EU_ORIGIN origin) {
			this.origin = origin;
		}
		
		public void setPartForOrigin() {
			this.origin = EU_ORIGIN.PART;
		}
		
		@Override public <T extends XOReplace> boolean exist(T comPr) {
			if (parent == comPr.getParent() && setter == comPr.getSetter() 
					&& value == comPr.getValue() && origin == ((PortalReplace) comPr).getOrigin())
				return true;
			return false;
		}
	}
	
	private static class PortalPair {
		private String props_name;
		private String xml_name;
		private boolean validate;
		public PortalPair(String props_name, String xml_name, boolean validate) {
			super();
			this.props_name = props_name;
			this.xml_name = xml_name;
			this.validate = validate;
		}
		public String getProps_name() {
			return props_name;
		}
		public String getXml_name() {
			return xml_name;
		}
		public boolean isValidate() {
			return validate;
		}
	}
}

