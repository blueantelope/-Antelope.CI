// com.antelope.ci.bus.portal.core.configuration.xo.form.Content.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-11		下午2:47:51 
 */
@XmlEntity(name="content")
public class Content implements Serializable {
	private Title title;
	private Attribute attribute;
	private List<Group> groupList = new ArrayList<Group>();
	
	@XmlElement(name="title")
	public Title getTitle() {
		return title;
	}
	public void setTitle(Title title) {
		this.title = title;
	}
	
	@XmlElement(name="attribute")
	public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	
	@XmlElement(name="group", isList=true, listClass=Group.class)
	public List<Group> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}
	
	public Widget getFocusWidget() {
		Component first_com = null;
		if (attribute != null && !StringUtil.empty(attribute.getFocus())) {
			String focus = attribute.getFocus();
			if (focus.contains(Attribute.FOCUS_DECOLLATOR)) {
				String[] fs = focus.split(Attribute.FOCUS_DECOLLATOR);
				if (fs.length == 2) {
					String com = fs[0];
					String wid = fs[1];
					for (Group group : groupList) {
						for (Component component : group.getComponentList()) {
							if (first_com == null)
								first_com = component;
							if (com.equalsIgnoreCase(component.getName())) {
								if ("label".equalsIgnoreCase(wid))
									return component.getLabel();
								else
									return component.getField();
							}
						}
					}
				}
			}
		}
		
		if (first_com != null) return first_com.getField();
		
		return null;
	}
}