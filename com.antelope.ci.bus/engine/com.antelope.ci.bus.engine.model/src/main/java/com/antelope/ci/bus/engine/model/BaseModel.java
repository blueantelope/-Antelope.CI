// com.antelope.ci.bus.engine.model.BaseModel.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.BusConstants;
import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.api.ApiMessage;
import com.antelope.ci.bus.common.api.BT;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年12月5日		上午9:28:14 
 */
public abstract class BaseModel implements Serializable, IModel {
	private static final Logger log = Logger.getLogger(BaseModel.class);
	protected ApiMessage message;
	protected List<Map<String, String>> bodyList;

	public BaseModel() {
		super();
		message = new ApiMessage();
		init();
	}
	
	public void fromMap(Map<String, String> modelMap) throws CIBusException {
		String className = StringUtil.getLastName(this.getClass().getName(), "//"+BusConstants.DOT);
		for (String name : modelMap.keySet()) {
			String[] ns = name.split(".");
			if (ns.length == 2) {
				String clsName = ns[0];
				String funName = ns[1];
				funName = "set" + funName.substring(0, 1).toUpperCase() + funName.substring(1); 
				if (clsName.equalsIgnoreCase(className))
					ProxyUtil.invoke(this, funName, new Object[]{modelMap.get(name)});
			}
		}
	}
	
	
	public List<Map<String, String>> parseBody() throws CIBusException {
		if (bodyList == null)
			bodyList = ModelUtil.parseBody(message);
		return bodyList;
	}
	
	@Override
	public String toJson() {
		return genJson();
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.model.IModel#fromJson(java.lang.String)
	 */
	@Override
	public void fromJson(String json) throws CIBusException {
		// TODO: generate
	}
	
	public ApiMessage getMessage() {
		return message;
	}
	
	@Override
	public ApiMessage toMessage() {
		short bt = message.getBt();
		switch (bt) {
			case BT._binary:
				
				break;
			case BT._json:
				String json = genJson();
				message.setBody(json.getBytes());
				break;
		}
		return message;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.model.IModel#fromMessage(com.antelope.ci.bus.common.api.ApiMessage)
	 */
	@Override
	public void fromMessage(ApiMessage message) throws CIBusException {
		this.message = message;
		if (bodyList == null)
			ModelUtil.parseBody(message);
		if (bodyList == null || bodyList.isEmpty())
			return;
		
		Map<String, String> vMap = ModelUtil.makeValueMap(bodyList);
		if (vMap.isEmpty())
			return;
		
		Map<String, Method> setterMap = ModelUtil.fetchSetter(this);
		for (String name : vMap.keySet()) {
			if (setterMap.containsKey(name))
				ProxyUtil.invokeSetter(this, setterMap.get(name), vMap.get(name));
		}
	}
	
	protected String genJson() {
		Field[] fields = this.getClass().getDeclaredFields();
		Map<String, String> tempMap = new HashMap<String, String>(); 
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				if (field.isAnnotationPresent(ModelData.class)) {
					ModelData mdata = field.getAnnotation(ModelData.class);
					String name = mdata.name();
					if (StringUtil.empty(name)) 
						name = field.getName();
					String getter = mdata.getter();
					Object value;
					if (StringUtil.empty(getter))
						value = field.get(this);
					else
						value = ProxyUtil.invokeRet(this, getter);
					tempMap.put(name, genJson(value));
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
				log.warn(e);
			}
		}
		
		StringBuilder jsonBuilder = new StringBuilder();
		if (!tempMap.isEmpty()) {
			jsonBuilder.append("{");
			boolean first = true;
			for (String name : tempMap.keySet()) {
				if (first)
					first = false;
				else
					jsonBuilder.append(";");
				jsonBuilder.append("\"").append(name).append("\":\"").append(tempMap.get(name)).append("\"");
				
			}
			jsonBuilder.append("}");
		}
		
		return jsonBuilder.toString();
	}
	
	protected String genJson(Object value) {
		if (value == null)
			return "";
		
		if (Collection.class.isAssignableFrom(value.getClass())) {
			Collection col = (Collection) value;
			if (col.size() > 0) {
				StringBuilder colBuilder = new StringBuilder();
				colBuilder.append("[");
				for (Object o : col)
					colBuilder.append(genJson(o));
				colBuilder.append("]");
				return colBuilder.toString();
			} 
		} else if (value.getClass().isAnnotationPresent(Model.class)) {
			return ((IModel) value).toJson();
		} else {
			return String.valueOf(value);
		}
		
		return "";
	}
	
	protected abstract void init();
}
