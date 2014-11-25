// com.antelope.ci.bus.portal.core.configuration.xo.Form.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Component;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Content;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Group;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Input;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Style;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Widget;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_InputLevel;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Widget;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-11		上午11:52:50 
 */
@XmlEntity(name="form")
public class Form implements Serializable {
	private static final Logger log = Logger.getLogger(Form.class);
	private Style style;
	private List<Input> inputList = new ArrayList<Input>();
	private Content content;

	@XmlElement(name="style")
	public Style getStyle() {
		return style;
	}
	public void setStyle(Style style) {
		this.style = style;
	}
	
	@XmlElement(name="input", isList=true, listClass=Input.class)
	public List<Input> getInputList() {
		return inputList;
	}
	public void setInputList(List<Input> inputList) {
		this.inputList = inputList;
	}
	
	@XmlElement(name="content")
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	
	public void redress() {
		if (null != content) {
			for (Group group : content.getGroupList()) {
				for (Component component : group.getComponentList()) {
					EU_InputLevel iLevel = component.getInputLevel();
					InputTypePair inputPair = null;
					switch (iLevel) {
						case DEFAULT:
							inputPair = getDefaultInput(component.getType());
							break;
						case COMMON:
							inputPair = getDefaultInput(component.getType(), component.getInputName());
							break;
						default:
							break;
					}
					if (null != inputPair) {
						Input input = inputPair.input;
						component.setInput(input);
						try {
							EU_Widget eu_widget = input.toWidget();
							Widget widget = null;
							switch (eu_widget) {
								case LABEL:
									widget = component.getLabel();
									break;
								case FIELD:
									widget = component.getField();
									break;
								default:
									break;
							}
							if (null != widget)
								widget.setBox(input.getBox());
						} catch (CIBusException e) {
							log.error(e);
							DevAssistant.errorln(e);
						}
					}
				}
			}
		}
	}

	private InputTypePair getDefaultInput(String type) {
		return getDefaultInput(type, "_default");
	}
	
	private InputTypePair getDefaultInput(String type, String name) {
		for (Input input : inputList) {
			if (input.getType().equals(type) && input.getName().equalsIgnoreCase(name))
				return new InputTypePair(input.getType(), input);
		}
		
		return null;
	}
	
	public List<String> getComponentNameList() {
		List<String> nameList = new ArrayList<String>();
		if (content != null) {
			for (Group group : content.getGroupList()) {
				for (Component component : group.getComponentList()) {
					if (!StringUtil.empty(component.getName()))
						nameList.add(component.getName());
				}
			}
		}
		
		return nameList;
	}
	
	private static class InputTypePair {
		private String type;
		private Input input;
		
		public InputTypePair(String type, Input input) {
			super();
			this.type = type;
			this.input = input;
		}
	}
}

