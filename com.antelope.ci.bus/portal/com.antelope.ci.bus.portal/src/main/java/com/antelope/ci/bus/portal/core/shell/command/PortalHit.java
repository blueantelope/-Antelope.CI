// com.antelope.ci.bus.portal.core.shell.command.MainCommonPortalHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.command;

import java.lang.reflect.Method;
import java.util.List;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusActivator;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.form.CommonFormAction;
import com.antelope.ci.bus.portal.core.shell.form.FormAction;
import com.antelope.ci.bus.portal.core.shell.form.FormCommand;
import com.antelope.ci.bus.portal.core.shell.form.PortalFormContext;
import com.antelope.ci.bus.portal.core.shell.form.PortalFormContextFactory;
import com.antelope.ci.bus.server.shell.base.BusShell;
import com.antelope.ci.bus.server.shell.base.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandAdapter;
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
	
	protected String[] splitCommand() {
		Command command = this.getClass().getAnnotation(Command.class);
		return command.commands().split(",");
	}
	
	protected boolean commandEqual(String[] commands1, String[] commands2) {
		for (String command1 : commands1) {
			for (String command2 : commands2) {
				if (command1.equals(command2))
					return true;
			}
		}
		
		return false;
	}
	
	protected QueryCommand queryCommand(ClassLoader loader, String mode, String[] commands, List<String> classList) {
		for (String clsname : classList) {
			try {
				Class clazz = loader.loadClass(clsname);
				if (clazz.isAnnotationPresent(FormAction.class) && CommonFormAction.class.isAssignableFrom(clazz)) {
					for (Method method : clazz.getMethods()) {
						if (method.isAnnotationPresent(FormCommand.class)) {
							FormCommand formCommand = (FormCommand) method.getAnnotation(FormCommand.class);
							if (formCommand.mode().equals(mode)) {
								String[] formCommands = formCommand.commands().split(",");
								if (commandEqual(formCommands, commands))
									return new QueryCommand(clsname, method.getName());
							} 
						}
					}
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		
		return null;
	}
	
	private static class QueryCommand {
		String className;
		String function;
		public QueryCommand(String className, String function) {
			super();
			this.className = className;
			this.function = function;
		}
	}
	
	protected String invokeFormAction(BusPortalShell shell,  Object... args) throws CIBusException {
		String mode = shell.getMode();
		String[] commands = splitCommand();
		ClassLoader loader = null;
		try {
			loader = shell.getClassLoader();
		} catch (Exception e) {
			DevAssistant.errorln(e);
		}
		if (loader == null)
			loader = CommandAdapter.class.getClassLoader();
		List<String>  classList = ClassFinder.findClasspath("com.antelope.ci.bus.portal", loader);
		QueryCommand queryCommand = queryCommand(loader, mode, commands, classList);
		
		if (queryCommand != null) {
			CommonFormAction formAction = (CommonFormAction) ProxyUtil.newObject(queryCommand.className, loader);
			formAction.setShell(shell);
			formAction.setArgs(args);
			return (String) ProxyUtil.invokeRet(formAction, queryCommand.function);
		}
		return BusShellStatus.KEEP;
	}
	
	protected abstract String invoke(BusPortalShell shell,  Object... args);
}

