// com.antelope.ci.bus.portal.core.shell.command.PortalHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.BusPortalFormHelper;
import com.antelope.ci.bus.portal.core.configuration.xo.Form;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Style;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Title;
import com.antelope.ci.bus.portal.core.shell.ShellLineContentSet;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.hit.Hit;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-28		上午10:44:48 
 */
public abstract class PortalHit extends Hit {
	protected Form form;
	protected Properties properties;
	
	public PortalHit() {
		init();
	}
	
	private void init() {
		if (this.getClass().isAnnotationPresent(Command.class)) {
			Command cmd = this.getClass().getAnnotation(Command.class);
			if (!StringUtil.empty(cmd.form())) {
				String form_xml = cmd.form();
				form = BusPortalFormHelper.getForm(form_xml);
				if (form == null) {
					try {
						form = BusPortalFormHelper.loadForm(form_xml, this.getClass());
					} catch (CIBusException e) {
						e.printStackTrace();
					}
				}
			}
			if (!StringUtil.empty(cmd.property())) {
				String property = cmd.property();
				properties = BusPortalFormHelper.getProperties(property);
				if (properties == null) {
					try {
						properties = BusPortalFormHelper.loadProperties(property);
					} catch (CIBusException e) {
						e.printStackTrace();
					}
				}
			}
			
			if (form != null && properties != null)
				BusPortalFormHelper.convert(form, properties);
		}
	}
	
	protected void draw(BusShell shell) throws IOException {
		if (form != null) {
			ShellLineContentSet contentSet = new ShellLineContentSet();
			Style formStyle = form.getStyle();
			// add title to form
			Title title = form.getContent().getTitle();
			if (title != null) {
				Style titleStyle = title.getStyle();
				if (titleStyle == null) {
					title.setStyle(titleStyle);
					titleStyle = formStyle;
				}
				List<String> line = new ArrayList<String>();
				line.add(title.toShellText().toString());
				contentSet.addLine(line);
			}
			
			shell.writeContent(contentSet);
		}
	}
}

