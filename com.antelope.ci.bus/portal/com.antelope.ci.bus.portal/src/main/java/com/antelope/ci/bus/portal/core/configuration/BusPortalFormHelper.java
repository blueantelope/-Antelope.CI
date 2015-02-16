// com.antelope.ci.bus.portal.configuration.BusPortalFormHelper.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.ResourceUtil;
import com.antelope.ci.bus.common.configration.IsolateResourceReader;
import com.antelope.ci.bus.common.configration.ResourceReader;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;
import com.antelope.ci.bus.portal.core.configuration.xo.Form;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-11		上午11:45:23 
 */
public class BusPortalFormHelper {
	private static final String FORM_XSD = "/com/antelope/ci/bus/portal/core/configuration/form.xsd";
	private static InputStream FORM_XSD_IN;
	private static Map<String, Form> formMap;
	private static Map<String, Properties> propsMap;
	private static ResourceReader reader;
	private static ClassLoader _classLoader;
	
	static {
		FORM_XSD_IN = BusPortalFormHelper.class.getResourceAsStream(FORM_XSD);
		formMap = new ConcurrentHashMap<String, Form>();
		propsMap = new ConcurrentHashMap<String, Properties>();
		reader = new IsolateResourceReader();
	}
	
	public static void initClassLoader(ClassLoader classLoader) {
		if (_classLoader == null)
			_classLoader = classLoader;
	}
	
	public static Form getForm(String name) {
		return formMap.get(name);
	}
	
	public static Form loadForm(String form_xml, Class cls) throws CIBusException {
		InputStream form_in = ResourceUtil.getXmlStream(cls, form_xml);
		Form form = parse(form_in);
		form.redress();
		return form;
	}
	
	public static Properties getProperties(String name) {
		return propsMap.get(name);
	}
	
	public static Properties loadProperties(String name) throws CIBusException {
		try {
			reader.addConfig(name);
		} catch(Exception e) {
			DevAssistant.errorln(e);
			reader.addConfig(name, _classLoader);
		}
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
	
	public static void convert(Form form, Properties props) throws CIBusException {
		List<FormReplace> replaceList = genFormReplaceList(form);
		for (FormReplace fr : replaceList) {
			String new_value = (String) fr.getValue();
			if (ResourceUtil.needReplace(new_value)) {
				new_value = ResourceUtil.replaceLableForProperties(new_value, props);
				ProxyUtil.invoke(fr.getParent(), fr.getSetter(), new Object[]{new_value});
			}
		}
	}
	
	private static List<FormReplace> genFormReplaceList(Form form) {
		List<FormReplace> replaceList = new ArrayList<FormReplace>();
		FormReplaceTree<FormReplace, FormReplaceTree> tree = genFormReplaceTree(form);
		genFormReplaceList(replaceList, tree);
		return replaceList;
	}
	
	private static FormReplaceTree genFormReplaceTree(Form form) {
		FormReplaceTree<FormReplace, FormReplaceTree> tree = new FormReplaceTree<FormReplace, FormReplaceTree>();
		tree.isRoot();
		genFormReplaceTree(tree, form);
		return tree;
	}
	
	private static void genFormReplaceTree(FormReplaceTree tree, Object root) {
		List<FormReplaceTree<FormReplace, FormReplaceTree>> deepList = new ArrayList<FormReplaceTree<FormReplace, FormReplaceTree>>();
		for (Method method : root.getClass().getMethods()) {
			if (method.getName().startsWith("get") 
					&& !method.getName().startsWith("getClass") 
					&& !ProxyUtil.hasArguments(method)) {
				try {
					Object o = ProxyUtil.invokeRet(root, method);
					if (o == null)
						continue;
					if (List.class.isAssignableFrom(o.getClass())) {
						FormReplace child_pr = new FormReplace(root, "", o);
						FormReplaceTree child = new FormReplaceTree();
						child.setValue(child_pr);
						child.isList();
						tree.addChild(child);
						deepList.add(child);
					} else {
						String setter = "set" + method.getName().substring(3);
						Method setMethod = null;
						for (Method m : root.getClass().getMethods()) {
							if (m.getName().equals(setter)) {
								setMethod = m;
								break;
							}
						}
						if (setMethod != null) {
							boolean isStringValue = method.getReturnType() == String.class;
							FormReplace child_pr = new FormReplace(root, setter, o);
							FormReplaceTree child = new FormReplaceTree();
							child.setValue(child_pr);
							if (isStringValue)
								child.isStringValue();
							tree.addChild(child);
							deepList.add(child);
						}
					}
				} catch (Exception e) {
					DevAssistant.assert_exception(e);
				}
			}
		}
		for (FormReplaceTree deep : deepList) {
			if (deep.isList) {
				List list =(List) deep.getValue().getValue();
				for (Object o : list)
					if (deep.getValue() != null)
						genFormReplaceTree(deep, o);
			} else {
				if (deep.getValue() != null)
					genFormReplaceTree(deep, deep.getValue().getValue());
			}
		}
	}
	
	private static void genFormReplaceList(List<FormReplace> replaceList, FormReplaceTree<FormReplace, FormReplaceTree> tree) {
		if (tree.hasValue()) {
			FormReplace pr = (FormReplace) tree.getValue();
			boolean added = true;
			for (FormReplace existPr : replaceList) {
				if (pr.exist(existPr)) {
					added = false;
					break;
				}
			}
			if (added)
				replaceList.add(pr);
		}
		
		for (Object child : tree.getChildren())
			genFormReplaceList(replaceList, (FormReplaceTree) child);
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

