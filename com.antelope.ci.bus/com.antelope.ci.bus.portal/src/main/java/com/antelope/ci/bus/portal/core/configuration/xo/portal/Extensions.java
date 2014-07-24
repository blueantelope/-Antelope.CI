// com.antelope.ci.bus.portal.configuration.xo.Extensions.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.Serializable;
import java.util.List;

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Point;


/**
 * 
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-17		下午4:29:20 
 */
@XmlEntity(name="extensions")
public class Extensions implements Serializable {
	private List<Extension> extentionList;

	@XmlElement(name="extension", isList=true, listClass=Extension.class)
	public List<Extension> getExtentionList() {
		return extentionList;
	}

	public void setExtentionList(List<Extension> extentionList) {
		this.extentionList = extentionList;
	}
	
	public Base getBase() {
		if (extentionList != null && !extentionList.isEmpty()) {
			for (Extension ext : extentionList) {
				if (ext.getPoint() == EU_Point.BASE)
					return ext.getBase();
			}
		}
		
		return null;
	}
	
	public int getOrder() {
		Base b = getBase();
		if (b != null)		return b.getOrder();
		else					return -1;
	}
	
}

