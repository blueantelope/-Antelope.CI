// com.antelope.ci.bus.portal.configuration.xo.Layout.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		上午11:27:45 
 */
@XmlEntity(name="layout")
public class Layout {
	private List<Place> placeList;

	@XmlElement(name="place", isList=true, listClass=Place.class)
	public List<Place> getPlaceList() {
		return placeList;
	}
	public void setPlaceList(List<Place> placeList) {
		this.placeList = placeList;
	}
	
	public void addPlace(Place place) {
		if (placeList == null) placeList = new ArrayList<Place>();
		placeList.add(place);
	}
	
	public Place getPlace(String name) {
		if (placeList != null) {
			for (Place p : placeList) {
				if (p.getName().equals(name))
					return p;
			}
		}
		
		return null;
	}
}

