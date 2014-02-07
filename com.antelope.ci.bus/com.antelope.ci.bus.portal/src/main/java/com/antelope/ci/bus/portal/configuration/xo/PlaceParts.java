// com.antelope.ci.bus.portal.configuration.xo.PlaceParts.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration.xo;

import java.util.List;

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
public class PlaceParts {
	private List<PlacePart> partList;

	@XmlElement(name="part", isList=true, listClass=PlacePart.class)
	public List<PlacePart> getPartList() {
		return partList;
	}

	public void setPartList(List<PlacePart> partList) {
		this.partList = partList;
	}
}

