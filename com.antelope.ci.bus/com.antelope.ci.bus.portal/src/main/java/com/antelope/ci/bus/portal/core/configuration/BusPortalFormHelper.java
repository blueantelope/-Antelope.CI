// com.antelope.ci.bus.portal.configuration.BusPortalFormHelper.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.antelope.ci.bus.common.ResourceUtil;
import com.antelope.ci.bus.common.configration.IsolateResourceReader;
import com.antelope.ci.bus.common.configration.ResourceReader;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;
import com.antelope.ci.bus.portal.core.configuration.xo.Form;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-11		上午11:45:23 
 */
public class BusPortalFormHelper {
	private static final String FORM_XSD = "/com/antelope/ci/bus/portal/core/configuration/form.xsd";
	private static InputStream FORM_XSD_IN = null;
	private static Map<String, Form> formMap;
	private static Map<String, Properties> propsMap;
	private static ResourceReader reader;
	
	static {
		FORM_XSD_IN = BusPortalFormHelper.class.getResourceAsStream(FORM_XSD);
		formMap = new ConcurrentHashMap<String, Form>();
		propsMap = new ConcurrentHashMap<String, Properties>();
		reader = new IsolateResourceReader();
	}
	
	public static Form getForm(String name) {
		return formMap.get(name);
	}
	
	public static Form loadForm(String form_xml, Class cls) throws CIBusException {
		InputStream form_in = ResourceUtil.getXmlStream(cls, form_xml);
		Form form = parse(form_in);
		return form;
	}
	
	public static Properties getProperties(String name) {
		return propsMap.get(name);
	}
	
	public static Properties loadProperties(String name) throws CIBusException {
		reader.addConfig(name);
		Properties properties = reader.getIsolateProps(name);
		return properties;
	}

	/**
	 * first validate and parse, convert to xo
	 * @param  @param form_im
	 * @param  @return
	 * @param  @throws CIBusException
	 * @return Form
	 * @throws
	 */
	public static Form validateAndParse(InputStream form_im) throws CIBusException {
		if (FORM_XSD_IN == null) 
			throw new CIBusException("", "xsd file not been found");
		return (Form) BusXmlHelper.parse(Form.class, form_im, FORM_XSD_IN);
	}
	
	/**
	 * no need to validate, parse directly and convert to xo
	 * @param  @param form_in
	 * @param  @return
	 * @param  @throws CIBusException
	 * @return Form
	 * @throws
	 */
	public static Form parse(InputStream form_in) throws CIBusException {
		return (Form) BusXmlHelper.parse(Form.class, form_in);
	}
	
	public static void convert(Form form, Properties props) {
		
	}
	
	private List<FormReplace> genFormReplaceList(Form form) {
		List<FormReplace> replaceList = new ArrayList<FormReplace>();
		
		return replaceList;
	}
	
	private static class FormReplaceTree<R extends FormReplace, Tree extends FormReplaceTree> extends XOReplaceTree {
		public FormReplaceTree() {
			super();
		}
	}
	
	private static class FormReplace extends XOReplace {
		public FormReplace(Object parent, String setter, Object value) {
			super(parent, setter, value);
		}
	}
}

