// com.antelope.ci.bus.portal.configuration.BusPortalFormHelper.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
	private static final String FORM_XSD = "/com/antelope/ci/bus/portal/configuration/form.xsd";
	private static InputStream FORM_XSD_IN = null;
	
	static {
		try {
			FORM_XSD_IN = new FileInputStream(FORM_XSD);
		} catch (FileNotFoundException e) {
		}
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
}

