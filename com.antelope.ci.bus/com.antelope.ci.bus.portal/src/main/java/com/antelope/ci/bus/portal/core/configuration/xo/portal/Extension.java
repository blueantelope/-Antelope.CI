// com.antelope.ci.bus.portal.configuration.xo.Extension.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.Serializable;
import java.util.List;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Point;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-17		下午4:29:55 
 */
@XmlEntity(name="extension")
public class Extension implements Serializable {
	private String point;
	private Base base;
	private Action action;
	private List<PlaceParts> placePartList;
	private List<Part> partList;
	
	@XmlAttribute(name="point")
	public String getPoint() {
		return point;
	}
	public void setPoint_exp(String point) {
		this.point = point;
	}
	public EU_Point toPoint() throws CIBusException {
		return EU_Point.toPoint(point);
	}
	
	@XmlElement(name="base")
	public Base getBase() {
		return base;
	}
	public void setBase(Base base) {
		this.base = base;
	}
	
	@XmlElement(name="action")
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	
	@XmlElement(name="parts", isList=true, listClass=PlaceParts.class)
	public List<PlaceParts> getPlacePartList() {
		return placePartList;
	}
	public void setPlacePartList(List<PlaceParts> placePartList) {
		this.placePartList = placePartList;
	}
	
	@XmlElement(name="part", isList=true, listClass=Part.class)
	public List<Part> getPartList() {
		return partList;
	}

	public void setPartList(List<Part> partList) {
		this.partList = partList;
	}
}