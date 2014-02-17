// com.antelope.ci.bus.portal.configuration.xo.Extension.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import java.util.List;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-17		下午4:29:55 
 */
@XmlEntity(name="extension")
public class Extension {
	private String point_exp;
	private EU_Point point;
	private List<PlaceParts> palcePartList;
	private List<Part> partList;

	@XmlAttribute(name="point")
	public String getPoint_exp() {
		return point_exp;
	}
	public void setPoint_exp(String point_exp) {
		this.point_exp = point_exp;
		try {
			this.point = EU_Point.toPoint(this.point_exp);
		} catch (CIBusException e) {
		}
	}
	
	public EU_Point getPoint() {
		return point;
	}
	
	@XmlElement(name="parts", isList=true, listClass=PlaceParts.class)
	public List<PlaceParts> getPalcePartList() {
		return palcePartList;
	}
	public void setPalcePartList(List<PlaceParts> palcePartList) {
		this.palcePartList = palcePartList;
	}
	
	@XmlElement(name="part", isList=true, listClass=Part.class)
	public List<Part> getPartList() {
		return partList;
	}

	public void setPartList(List<Part> partList) {
		this.partList = partList;
	}
	
	
}

