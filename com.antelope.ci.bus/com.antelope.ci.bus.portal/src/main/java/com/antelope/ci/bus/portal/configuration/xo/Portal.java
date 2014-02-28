// com.antelope.ci.bus.portal.configuration.xo.Portal.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;
import com.antelope.ci.bus.common.xml.BusXmlHelper.SetterGetterPair;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		上午11:25:05 
 */
@XmlEntity(name="portal")
public class Portal {
	private Base base;
	private Layout layout;
	private Parts parts;
	private Extensions extensions;
	
	public String getName() {
		String name = "null";
		if (base != null && base.getName() != null)
			name = base.getName();
		return name;
	}
	
	@XmlElement(name="base")
	public Base getBase() {
		return base;
	}
	public void setBase(Base base) {
		this.base = base;
	}
	
	@XmlElement(name="layout")
	public Layout getLayout() {
		return layout;
	}
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	
	@XmlElement(name="parts")
	public Parts getParts() {
		return parts;
	}
	public void setParts(Parts parts) {
		this.parts = parts;
	}
	
	@XmlElement(name="extensions")
	public Extensions getExtensions() {
		return extensions;
	}
	public void setExtensions(Extensions extensions) {
		this.extensions = extensions;
	}
	
	public void attachExtensions() {
		if (extensions != null) {
			if (extensions.getExtentionList() != null) {
				for (Extension ext : extensions.getExtentionList()) {
					switch (ext.getPoint()) {
						case 	BASE:
							attachBase(ext.getBase());
							break;
						case LAYOUT:
							List<PlaceParts> ppList_ext = ext.getPlacePartList();
							if (ppList_ext != null)
								attachLayout(ppList_ext);
							break;
						case PARTS:
							List<Part> pList_ext = ext.getPartList();
							if (pList_ext != null)
								attachPart(pList_ext);
							break;
					}
				}
			}
		}
	}
	
	private void attachBase(Base base_ext) {
		switch (base_ext.getEmbed()) {
			case REPLACE:
				base = base_ext;
				break;
			case APPEND:
				if (base == null) base = new Base();
				SetterGetterPair[] pairs = BusXmlHelper.FetchPairOfXml(Base.class);
				for (SetterGetterPair pair : pairs) {
					try {
						Object arg = ProxyUtil.invokeRet(pair.getGetter(), base_ext);
						if (arg != null) ProxyUtil.invoke(base, pair.getSetter(), new Object[]{arg});
					} catch (CIBusException e) {
						DevAssistant.errorln(e);
					}
				}
				break;
		}
	}
	
	private void attachLayout(List<PlaceParts> ppList_ext) {
		for (PlaceParts pp_ext : ppList_ext) {
			String pp_ename = pp_ext.getName();
			Place pp = null;
			Place p = null;
			List<Place> pList = null;
			if (pp_ename.contains(".")) {
				String[] pps = pp_ename.split(".");
				pp = layout.getPlace(pps[0]);
				if (pp != null) {
					p = pp.getParts().getPlaceMap().get(pps[1]);
					if (pp.getParts() != null) 
						pList = pp.getParts().getPlaceList();
				}
			} else {
				p = layout.getPlace(pp_ename);
				pList = layout.getPlaceList();
			}
			switch (pp_ext.getEmbed()) {
				case REPLACE:
					if (p != null) p.setParts(pp_ext);
					break;
				case APPEND:
					if (p != null) {
						p.getParts().addParts(pp_ext.getPartList());
					} else {
						p = new Place();
						p.setParts(pp_ext);
						if (pList == null) pList = new ArrayList<Place>();
						pList.add(p);
					}
					break;
			}
		}
	}
	
	private void attachPart(List<Part> pList_ext) {
		Map<String, Part> partMap = getPartMap();
		for (Part p_ext : pList_ext) {
			String p_ext_name = p_ext.getName();
			Part p = partMap.get(p_ext_name);
			switch (p_ext.getEmbed()) {
				case REPLACE:
					if (p != null)
						p = p_ext;
					break;
				case APPEND:
					if (p == null) {
						if (parts == null) parts = new Parts();
						List<Part> partList = parts.getPartList();
						if (partList == null) partList = new ArrayList<Part>();
						if (p_ext.getSort() == null) {
							partList.add(p);
						} else {
							int s_index = p_ext.getSort();
							int p_len = partList.size();
							s_index = s_index > (p_len - 1) ? p_len : s_index;
							partList.add(s_index, p);
						}
					} else {
						if (p_ext.getSort() == null)
							p.addContet(p_ext.getContent());
						else
							p.addContet(p_ext.getSort(), p_ext.getContent());
					}
					break;
			}
		}
	}
	
	public void addPart(Part part) throws CIBusException {
		if (parts == null)
			parts = new Parts();
		if (parts.getPartList() == null)
			parts.setPartList(new ArrayList<Part>());
		for (Part p : parts.getPartList()) {
			if (p.getName().trim().equalsIgnoreCase(part.getName().trim()))
				throw new CIBusException("part has exist");
		}
		parts.getPartList().add(part);
	}
	
	public Map<String, List<PlacePart>> getPlaceList() {
		Map<String, List<PlacePart>> placeList = new HashMap<String, List<PlacePart>>();
		if (layout != null) {
			if (layout.getPlaceList() != null) {
				for (Place place : layout.getPlaceList()) {
					String pName = place.getName();
					if (place.getParts() != null) {
						if (place.getParts().getPartList() != null && !place.getParts().getPartList().isEmpty()) {
							placeList.put(pName, place.getParts().getPartList());
						}
					}
				}
			}
		}
		
		return placeList;
	}
	
	public Map<String, Map<String, PlacePart>> getPlaceMap() {
		Map<String, Map<String, PlacePart>> placeMap = new HashMap<String, Map<String, PlacePart>>();
		Map<String, List<PlacePart>> placeList = getPlaceList();
		for (String key : placeList.keySet()) {
			Map<String, PlacePart> partMap = new HashMap<String, PlacePart>();
			for (PlacePart part : placeList.get(key)) {
				partMap.put(part.getPlace(), part);
			}
			placeMap.put(key, partMap);
		}
		
		return placeMap;
	}
	
	public Map<String, PlacePart> getPlacePartMap() {
		Map<String, PlacePart> partMap = new HashMap<String, PlacePart>();
		Map<String, List<PlacePart>> placeMap = getPlaceList();
		for (String key : placeMap.keySet()) {
			for (PlacePart part : placeMap.get(key)) {
				partMap.put(key+"."+part.getPlace(), part);
			}
		}
		
		return partMap;
	}
	
	public Map<String, Part> getPartMap() {
		Map<String, Part> partMap = new HashMap<String, Part>();
		if (parts != null) {
			partMap = parts.getPartMap();
		}
		
		return partMap;
	}
	
	public PlacePart getPlacePart(String lplaceName, String lpartName) {
		Map<String, Map<String, PlacePart>> placeMap = getPlaceMap();
		if (placeMap.get(lplaceName) != null) {
			return placeMap.get(lplaceName).get(lpartName);
		}
		
		return null;
	}
}

