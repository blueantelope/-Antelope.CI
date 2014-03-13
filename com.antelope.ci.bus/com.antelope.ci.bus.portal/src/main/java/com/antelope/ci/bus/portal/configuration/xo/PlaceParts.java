// com.antelope.ci.bus.portal.configuration.xo.PlaceParts.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		下午7:55:12 
 */
@XmlEntity(name="parts")
public class PlaceParts implements Serializable {
	private String name;
	private List<PlacePart> partList;
	private List<Place> placeList;
	private boolean isExtention;
	private EU_Embed embed;
	private String embed_exp;

	public PlaceParts() {
		this.isExtention = false;
	}
	
	public boolean isExtention() {
		return isExtention;
	}
	
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.isExtention = true;
	}

	@XmlElement(name="part", isList=true, listClass=PlacePart.class)
	public List<PlacePart> getPartList() {
		return partList;
	}

	public void setPartList(List<PlacePart> partList) {
		this.partList = partList;
	}
	
	@XmlElement(name="place", isList=true, listClass=Place.class)
	public List<Place> getPlaceList() {
		return placeList;
	}
	public void setPlaceList(List<Place> placeList) {
		this.placeList = placeList;
	}
	
	@XmlAttribute(name="embed")
	public String getEmbed_exp() {
		return embed_exp;
	}
	public void setEmbed_exp(String embed_exp) {
		this.embed_exp = embed_exp;
		try {
			this.embed = EU_Embed.toEmbed(this.embed_exp);
		} catch (CIBusException e) {
			e.printStackTrace();
		}
	}

	public EU_Embed getEmbed() {
		return embed;
	}
	
	public Map<String, Place> getPlaceMap() {
		Map<String, Place> ppMap = new HashMap<String, Place>();
		if (placeList != null) {
			for (Place p : placeList)
				ppMap.put(p.getName(), p);
		}
		return ppMap;
	}
	
	public Place getPlace(String name) {
		return getPlaceMap().get(name);
	}
	
	public void addPart(PlacePart part) {
		if (partList == null) partList = new ArrayList<PlacePart>();
		partList.add(part);
	}
	
	public void addPartList(List<PlacePart> addList) {
		if (partList == null) partList = new ArrayList<PlacePart>();
		partList.addAll(addList);
	}
	
	public void addPlace(Place addPlace) {
		if (placeList == null) placeList = new ArrayList<Place>();
		placeList.add(addPlace);
	}
	
	public void appendParts(PlaceParts aParts) {
		appendParts(this, aParts);
	}
	
	private void appendParts(PlaceParts parts, PlaceParts aParts) {
		if (aParts.getPartList() != null) {
			for (PlacePart aPart : aParts.getPartList()) {
				if (parts.getPartList() != null) {
					for (PlacePart part : parts.getPartList()) {
						if (part.getPlace().equals(aPart.getPlace())) {
							part =  aPart;
							break;
						}
					}
				} else {
					addPart(aPart);
				}
			}
		}
		
		if (aParts.getPlaceList() != null) {
			for (Place aPlace : aParts.getPlaceList()) {
				if (parts.getPlaceList() != null) {
					for (Place place : parts.getPlaceList()) {
						if (place.getName().equals(aPlace.getName())) {
							PlaceParts ps = place.getParts();
							PlaceParts aps = aPlace.getParts();
							if (aps == null)
								break;
							if (ps == null) 
								place.setParts(aps);
							else
								appendParts(ps, aps);
							break;
						}
					}
				} else {
					addPlace(aPlace);
				}
			}
		}
	}
	
}

