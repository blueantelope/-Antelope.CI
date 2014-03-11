// com.antelope.ci.bus.portal.configuration.xo.PlacePartNode.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import java.io.Serializable;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-8		上午12:35:23 
 */
public class PlacePartNode implements Serializable {
	private PlacePart placePart;
	private Part part;
	
	public PlacePartNode() {
		super();
	}
	public PlacePartNode(PlacePart placePart, Part part) {
		super();
		this.placePart = placePart;
		this.part = part;
	}
	public PlacePart getPlacePart() {
		return placePart;
	}
	public void setPlacePart(PlacePart placePart) {
		this.placePart = placePart;
	}
	public Part getPart() {
		return part;
	}
	public void setPart(Part part) {
		this.part = part;
	}
	
	public String getPlace() {
		return placePart.getPlace();
	}
	
	public boolean isBlank() {
		if (part != null)
			return part.contentEmpty();
		return true;
	}
}

