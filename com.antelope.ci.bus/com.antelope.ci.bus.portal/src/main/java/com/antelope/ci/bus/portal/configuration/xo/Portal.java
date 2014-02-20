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

import com.antelope.ci.bus.common.exception.CIBusException;
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
	private Layout layout;
	private Parts parts;
	private Extensions extensions;
	
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
}

