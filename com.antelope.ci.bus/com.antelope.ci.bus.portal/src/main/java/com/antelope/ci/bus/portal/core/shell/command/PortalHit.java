// com.antelope.ci.bus.portal.core.shell.command.MainCommonPortalHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.command;

import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.form.PortalFormContext;
import com.antelope.ci.bus.portal.core.shell.form.PortalFormContextFactory;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.command.hit.Hit;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-26		上午11:04:50 
 */
public abstract class PortalHit extends Hit {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.BaseCommand#execute(com.antelope.ci.bus.server.shell.BusShell, java.lang.Object[])
	 */
	@Override protected String execute(BusShell shell, Object... args) {
		BusPortalShell portalShell = (BusPortalShell) shell;
		return invoke(portalShell, args);
	}
	
	protected void up(BusPortalShell shell) {
		shell.up();
	}
	
	protected void down(BusPortalShell shell) {
		shell.down();
	}
	
	protected void left(BusPortalShell shell) {
		shell.left();
	}
	
	protected void right(BusPortalShell shell) {
		shell.right();
	}
	
	protected void addFormContext(BusPortalShell shell, Class commandClass) {
		PortalFormContextFactory factory = PortalFormContextFactory.getFactory();
		PortalFormContext formContext = factory.add(shell, commandClass);
		if (formContext != null)
			shell.replaceActiveFormContext(formContext);
	}
	
	protected PortalFormContext getFormContext(BusPortalShell shell, String identity) {
		PortalFormContextFactory factory = PortalFormContextFactory.getFactory();
		return factory.getFormContext(shell, identity);
	}
	
	protected void enterFormCommand(BusPortalShell shell) {
		PortalFormContext formContext = shell.getActiveFormContext();
		if (formContext != null) {
			formContext.enterFormCommand();
			shell.startFormCommand();
		}
	}
	
	protected void exitFormCommand(BusPortalShell shell) {
		shell.finishFormCommandMode();
	}
	
	protected abstract String invoke(BusPortalShell shell,  Object... args);
}

