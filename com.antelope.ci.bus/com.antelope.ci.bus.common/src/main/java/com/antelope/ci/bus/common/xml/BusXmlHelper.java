// com.antelope.ci.bus.common.xml.BusXmlHelper.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * xml operation helper
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-14		上午11:43:43 
 */
public class BusXmlHelper {
	private final static String LANGUAGE = XMLConstants.W3C_XML_SCHEMA_NS_URI;
	
	public static SetterGetterPair[] FetchPairOfXml(Class<?> clazz) {
		List<SetterGetterPair> pairList = new ArrayList<SetterGetterPair>();
		for (Method getm : fetchGetOfXml(clazz)) {
			String set_name = "get" + getm.getName().substring(3);
			try {
				Method setm = clazz.getMethod(set_name, getm.getReturnType());
				if (setm  != null) pairList.add(new SetterGetterPair(setm, getm));
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		
		return pairList.toArray(new SetterGetterPair[pairList.size()]);
	}
	
	public static Method[] FetchSetOfXml(Class<?> clazz) {
		List<Method> setList = new ArrayList<Method>();
		for (Method gm : fetchGetOfXml(clazz)) {
			String set_name = "get" + gm.getName().substring(3);
			try {
				Method setm = clazz.getMethod(set_name, gm.getReturnType());
				if (setm  != null) setList.add(setm);
			} catch (Exception e) {
				
			}
		}
		
		return setList.toArray(new Method[setList.size()]);
	}
	
	public static Method[] fetchGetOfXml(Class<?> clazz) {
		List<Method> mList = new ArrayList<Method>();
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(XmlElement.class) 
					|| method.isAnnotationPresent(XmlAttribute.class)
					|| method.isAnnotationPresent(XmlCdata.class)
					|| method.isAnnotationPresent(XmlText.class))
				mList.add(method);
		}
		
		return mList.toArray(new Method[mList.size()]);
	}
	
	public static Object parse(Class<?> clazz, InputStream input, InputStream xsd_in) throws CIBusException {
		ValidateInfo val_info = validate(input, xsd_in);
		if (!val_info.isResult()) {
			System.out.println(val_info.getError());
			return null;
		}
		Object o = null;
		if (clazz.isAnnotationPresent(XmlEntity.class)) {
			Document document = load(input);
			XmlEntity xmlRoot = (XmlEntity) clazz.getAnnotation(XmlEntity.class);
			try {
				o = ProxyUtil.createObject(clazz);
				if (o != null)
					parseXml(document, o, "/"+xmlRoot.name(), clazz.getClassLoader());
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} 
		return o;
	}
	
	public static Object parse(Class<?> clazz, InputStream input) throws CIBusException {
		Object o = null;
		if (clazz.isAnnotationPresent(XmlEntity.class)) {
			Document document = load(input);
			XmlEntity xmlRoot = (XmlEntity) clazz.getAnnotation(XmlEntity.class);
			try {
				o = ProxyUtil.createObject(clazz);
				if (o != null)
					parseXml(document, o, "/"+xmlRoot.name(), clazz.getClassLoader());
			} catch (Exception e) {
				DevAssistant.errorln(e);
			} 
		} 
		return o;
	}
	
	public static ValidateInfo validate(InputStream xml_in, InputStream xsd_in) {
		try {
			SchemaFactory factory = SchemaFactory.newInstance(LANGUAGE);
			Schema schema = factory.newSchema(new StreamSource(xsd_in));	// schema验证文件
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xml_in));									// xml被验证文件
			return new ValidateInfo(true, "");
		} catch (Exception e) {
			System.out.println(e);
			return new ValidateInfo(false, e.getMessage());
		}
	}
	
	private static class ValidateInfo {
		public boolean result = false;
		public String error = "";
		public ValidateInfo(boolean result, String error) {
			super();
			this.result = result;
			this.error = error;
		}
		public boolean isResult() {
			return result;
		}
		public String getError() {
			return error;
		}
	}
	
	public static String replaceValue(String xml, String replace) throws CIBusException {
		Document document = load(xml);
		Element root = document.getRootElement();
		replaceValue(root, replace);
		
		return document.asXML();
	}

	private static void replaceValue(Element parent, String replace) {
		if (parent.getText() != null) {
			parent.setText(replace);
		} else {
			for (Object child : parent.elements())
				replaceValue((Element) child, replace);
		}
	}
	
	private static Document load(InputStream input) throws CIBusException {
		SAXReader reader = new SAXReader();
		try {
			return reader.read(input);
		} catch (DocumentException e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}
	
	private static Document load(String str) throws CIBusException {
		return load(new ByteArrayInputStream(str.getBytes()));
	}
	
	private static void parseXml(Document document, Object parent, String rootQuery, ClassLoader loader) throws CIBusException {
		Class clazz = parent.getClass();
		List<DomQuery> domQueryList = new ArrayList<DomQuery>();
		for (Method method : clazz.getMethods()) {
			String query = rootQuery;
			Class subClass = method.getReturnType();
			Method setter = ProxyUtil.getSetter(clazz, method);
			// xml element节点设置
			if (method.isAnnotationPresent(XmlElement.class)) {
				XmlElement xmlElement = method.getAnnotation(XmlElement.class);
				query += "/" + xmlElement.name();
				if (setter != null) {
					if (subClass.isAnnotationPresent(XmlEntity.class) || 
						Collection.class.isAssignableFrom(subClass)) {
						String className = subClass.getName();
						if (xmlElement.isList()) {	
							className = ArrayList.class.getName();		// 列表使用ArrayList做为属性
						}
						Object arg = ProxyUtil.createObject(className, loader);
						try {
							setter.invoke(parent, arg);		// 设置setter方法
						} catch (Exception e) {
							throw new CIBusException("", e);
						}		
						DomQuery domQuery = new DomQuery(xmlElement, arg, query);
						domQueryList.add(domQuery);		// 放入列表中,待一个object的全部属性设置完成后，再做子节点的解析设置
					} else {
						Element element = (Element) document.selectSingleNode(query);
						Object arg = ProxyUtil.convertString(subClass, element.getText());
						try {
							setter.invoke(parent, arg);
						} catch (Exception e) {
							throw new CIBusException("", e);
						} 
					}
				}
			}
			
			// xml 值域设置
			if (method.isAnnotationPresent(XmlText.class) || 
					method.isAnnotationPresent(XmlCdata.class)) {
				invokeParameter(parent, setter, method, subClass, document, query, 1);
			}
			
			// xml 属性设置
			if (method.isAnnotationPresent(XmlAttribute.class)) {
				invokeParameter(parent, setter, method, subClass, document, query, 2);
			}
		}
		
		// 返回的类型为ARXmlEntity定义类型及Colletion类型节点解析
		for (DomQuery domQuery : domQueryList) {
			XmlElement xmlElement = domQuery.getXmlElement();
			Object instance = domQuery.getInstance();
			String xmlQuery = domQuery.getXmlQuery();
			List<Element> elemnetList = document.selectNodes(xmlQuery);			// 子节点列表
			if (elemnetList.isEmpty()) continue;
			
			if (xmlElement.isList()) {				// 子节点是否为一个列表
				if (xmlElement.listClass().isAnnotationPresent(XmlEntity.class)) {
					XmlEntity childNode = (XmlEntity) xmlElement.listClass().getAnnotation(XmlEntity.class);
					for (Element element :elemnetList) {
						Object innerObject = ProxyUtil.createObject(xmlElement.listClass(), loader);
						((Collection) instance).add(innerObject);
						parseChildXml(element, innerObject, loader);
					}
				} 
			} else {			// 方法为普通ARXmlchild定义类型
				parseChildXml(elemnetList.get(0), instance, loader);
			}
		}
	}
	
	/*
	 * xml子节点解析
	 */
	private static void parseChildXml(Element parentEle, Object parentObj, ClassLoader loader) throws CIBusException {
		Class clazz = parentObj.getClass();
		List<ChildElement> childElementList = new ArrayList<ChildElement>();	// 子节点列表
		for (Method method : clazz.getMethods()) {
			Class subClass = method.getReturnType();
			Method setter = ProxyUtil.getSetter(clazz, method);
			// xml element节点设置
			if (method.isAnnotationPresent(XmlElement.class)) {
				XmlElement xmlElement = method.getAnnotation(XmlElement.class);
				String eleName = xmlElement.name();
				if (setter != null) {
					if (subClass.isAnnotationPresent(XmlEntity.class) || 
							Collection.class.isAssignableFrom(subClass)) {		// 返回的类型为ARXmlEntity定义类型及Colletion类型
						String className = subClass.getName();
						if (xmlElement.isList()) {	
							className = ArrayList.class.getName();		// 列表使用ArrayList做为属性
						}
						Object arg = ProxyUtil.createObject(className, loader);
						try {
							setter.invoke(parentObj, arg);		// 设置setter方法
						} catch (Exception e) {
							throw new CIBusException("", e);
						}
						List<Element> cEleList = parentEle.elements(eleName);
						for (Element cEle : cEleList) {
							// 放入列表中,待一个object的全部属性设置完成后，再做子节点的解析设置
							ChildElement childElement = new ChildElement(arg, cEle, xmlElement);
							childElementList.add(childElement);
						}
					} else {
						Element element = parentEle.element(eleName);
						if (element != null) {		// element在xml中有定义，才进行下面的转换成object动作
							Object arg = ProxyUtil.convertString(subClass, element.getText());
							try {
								setter.invoke(parentObj, arg);
							} catch (Exception e) {
								throw new CIBusException("", e);
							} 
						}
					}
				}
			}
			
			// xml 值域设置
			if (method.isAnnotationPresent(XmlText.class) ||  method.isAnnotationPresent(XmlCdata.class)) {
				invokeParameter(parentObj, setter, method, subClass, parentEle, 1);
			}
			
			// xml 属性设置
			if (method.isAnnotationPresent(XmlAttribute.class)) {
				invokeParameter(parentObj, setter, method, subClass,parentEle, 2);
			}
		}
		
		// 返回的类型为ARXmlEntity定义类型及Colletion类型节点解析
		for (ChildElement childElement : childElementList) {
			Object pObj = childElement.parentObj;		// 上级节点的实例
			Object innerObject = pObj;
			Element pEle = childElement.parentEle;		// 上级节点xml element
			XmlElement xmlElement = childElement.xmlElement;
			if (xmlElement.isList()) {				// 子节点是否为一个列表
				if (xmlElement.listClass().isAnnotationPresent(XmlEntity.class)) {
					XmlEntity childNode = (XmlEntity) xmlElement.listClass().getAnnotation(XmlEntity.class);
					innerObject = ProxyUtil.createObject(xmlElement.listClass(), loader);
					((Collection) pObj).add(innerObject);
				} 
			} 
			parseChildXml(pEle, innerObject, loader);
		}
	}
	
	/*
	 * 执行对像中的变量赋值操作
	 */
	private static void invokeParameter(Object parent, Method setter, Method method, Class subClass, 
			Document document, String xmlQuery, int type) throws CIBusException {
		Object arg;
		Element element = (Element) document.selectSingleNode(xmlQuery);
		switch (type) {
			case 1:		// 值域
				arg = ProxyUtil.convertString(subClass, element.getText());
				break;
			case 2:		// 属性
				arg = ProxyUtil.convertString(subClass, 
						element.attributeValue(method.getAnnotation(XmlAttribute.class).name()));
				break;
			default:	// 未知类型
				return;
		}
		try {
			setter.invoke(parent, arg);
		} catch (Exception e) {
			throw new CIBusException("", e);
		} 
	}
	
	private static void invokeParameter(Object parent, Method setter, Method method, Class subClass, 
			Element element, int type) throws CIBusException {
		Object arg;
		switch (type) {
			case 1:		// 值域
				arg = ProxyUtil.convertString(subClass, element.getText());
				break;
			case 2:		// 属性
				arg = ProxyUtil.convertString(subClass, 
						element.attributeValue(method.getAnnotation(XmlAttribute.class).name()));
				break;
			default:	// 未知类型
				return;
		}
		
		if (arg == null)
			return;
		
		try {
			setter.invoke(parent, arg);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	/*
	 * DOM查询类定义
	 */
	private static class DomQuery {
		private XmlElement xmlElement;		// 此节点的xml element annotion定义
		private Object instance;					// 上级节点的实例
		private String xmlQuery;					// 节点所在xml查询语言
	
		private DomQuery(XmlElement xmlElement, Object instance, String xmlQuery) {
			this.xmlElement = xmlElement;
			this.instance = instance;
			this.xmlQuery = xmlQuery;
		}

		public XmlElement getXmlElement() {
			return xmlElement;
		}

		public Object getInstance() {
			return instance;
		}

		public String getXmlQuery() {
			return xmlQuery;
		}
	}
	
	/*
	 * 子节点定义
	 */
	private static class ChildElement {
		private Object parentObj;						// 上级节点的实例
		private Element parentEle;						// 上级节点xml element
		private XmlElement xmlElement;				// 此节点的xml element annotion定义
		
		private ChildElement(Object parentObj, Element parentEle, XmlElement xmlElement) {
			super();
			this.parentObj = parentObj;
			this.parentEle = parentEle;
			this.xmlElement = xmlElement;
		}

		public Object getParentObj() {
			return parentObj;
		}

		public Element getParentEle() {
			return parentEle;
		}

		public XmlElement getXmlElement() {
			return xmlElement;
		}
	}
	
	public static class SetterGetterPair {
		private Method setter;
		private Method getter;
		
		public SetterGetterPair(Method setter, Method getter) {
			this.setter = setter;
			this.getter = getter;
		}
		
		public Method getGetter() {
			return getter;
		}

		public Method getSetter() {
			return setter;
		}
	}
}

