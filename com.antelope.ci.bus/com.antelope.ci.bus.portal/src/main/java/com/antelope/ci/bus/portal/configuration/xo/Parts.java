// com.antelope.ci.bus.portal.configuration.xo.Parts.java
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

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		上午11:28:04 
 */
@XmlEntity(name="parts")
public class Parts implements Serializable {
	private List<Part> partList;

	@XmlElement(name="part", isList=true, listClass=Part.class)
	public List<Part> getPartList() {
		return partList;
	}

	public void setPartList(List<Part> partList) {
		this.partList = partList;
	}
	
	public Map<String, Part> getPartMap() {
		Map<String, Part> partMap = new HashMap<String, Part>();
		if (partList != null) {
			for (Part part : partList) {
				if (part != null)
					partMap.put(part.getName(), part);
			}
		}
		
		return partMap;
	}
	
	public void addPart(Part addPart) {
		int index = -1;
		if (partList != null) {
			int n = 0;
			for (Part part : partList) {
				if (part.getName().equals(addPart.getName())) {
					index = n;
					break;
				}
				n++;
			}
			if (index != -1)
				partList.remove(index);
		} else {
			partList = new ArrayList<Part>();
		}
		if (index != -1)
			partList.add(index, addPart);
		else
			partList.add(addPart);
	}
}

