// com.antelope.ci.bus.engine.model.user.Domain.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model.user;

import java.util.HashSet;
import java.util.Set;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.model.BaseModel;
import com.antelope.ci.bus.engine.model.Model;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年4月7日		下午2:08:36 
 */
@Model
public class Domain extends BaseModel{
	private final static int ROOT = -1;
	public final static int BASETYPE_PORTAL_ID = 10;
	public final static int BASETYPE_PORTAL_SHELL_ID = 11;
	public final static int BASETYPE_GATE_ID = 20;
	public final static int BASETYPE_GATE_API_ID = 21;
	public final static int BASETYPE_GATE_SHELL_ID = 12;
	public final static int BASETYPE_APP_ID = 30;
	public final static int BASETYPE_APP_WEB_ID = 31;
	
	public enum DOMAIN_BASETYPE {
		PORTAL(BASETYPE_PORTAL_ID, "portal", ROOT),
		PORTAL_SHELL(BASETYPE_PORTAL_SHELL_ID, "shell.portal", BASETYPE_PORTAL_ID),
		GATE(BASETYPE_GATE_ID, "gate", ROOT),
		GATE_API(BASETYPE_GATE_API_ID, "api.gate", BASETYPE_GATE_ID),
		GATE_SHELL(BASETYPE_GATE_SHELL_ID, "shell.gate", BASETYPE_GATE_ID),
		APP(BASETYPE_APP_ID, "APP", ROOT),
		APP_WEB(BASETYPE_APP_WEB_ID, "web.app", BASETYPE_APP_ID);
		
		private int id;
		private String name;
		private int parent;;
		
		private DOMAIN_BASETYPE(int id, String name, int parent) {
			this.id = id;
			this.name = name;
			this.parent = parent;
		}
		
		public int getId() {
			return id;
		}
		
		public String getName() {
			return name;
		}
		
		public int getParent() {
			return parent;
		}
		
		public static boolean contain(int id) {
			for (DOMAIN_BASETYPE type : DOMAIN_BASETYPE.values())
				if (type.getId() == id) return true;
			return false;
		}
		
		public static DOMAIN_BASETYPE get(int id) {
			for (DOMAIN_BASETYPE type : DOMAIN_BASETYPE.values()) {
				if (type.getId() == id)
					return type;
			}
			
			return null;
		}
		
		public static DOMAIN_BASETYPE get(String name) {
			for (DOMAIN_BASETYPE type : DOMAIN_BASETYPE.values()) {
				if (type.getName().equalsIgnoreCase(name.trim()))
					return type;
			}
			
			return null;
		}
	}
	
	protected int id;
	protected String name;
	protected int parent;
	
	public Domain(String name) throws CIBusException {
		DOMAIN_BASETYPE basetype = DOMAIN_BASETYPE.get(id);
		init(basetype);
	}
	
	public Domain(int id) throws CIBusException {
		DOMAIN_BASETYPE basetype = DOMAIN_BASETYPE.get(id);
		init(basetype);
	}
	
	public Domain(int id, String name, int parent) throws CIBusException {
		if (DOMAIN_BASETYPE.contain(id))
			throw new CIBusException("", "Domain Type exist!");
		this.id = id;
		this.name = name;
		this.parent = parent;
	}
	
	private void init(DOMAIN_BASETYPE basetype) throws CIBusException {
		if (basetype != null) {
			this.id = basetype.getId();
			this.name = basetype.getName();
			this.parent = basetype.getParent();
		} else {
			throw new CIBusException("", "Base Domain Type Not Found!");
		}
	}
	
	// getter
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getParent() {
		return parent;
	}

	@Override public int hashCode() {
		return id;
	}

	// new instance of base type
	public static Domain newPortal() throws CIBusException {
		return new Domain(BASETYPE_PORTAL_ID);
	}
	public static Domain newPortalShell() throws CIBusException {
		return new Domain(BASETYPE_PORTAL_SHELL_ID);
	}
	public static Domain newGate() throws CIBusException {
		return new Domain(BASETYPE_GATE_ID);
	}
	public static Domain newGateApi() throws CIBusException {
		return new Domain(BASETYPE_GATE_API_ID);
	}
	public static Domain newGateShell() throws CIBusException {
		return new Domain(BASETYPE_GATE_SHELL_ID);
	}
	public static Domain newApp() throws CIBusException {
		return new Domain(BASETYPE_APP_ID);
	}
	public static Domain newAppWeb() throws CIBusException {
		return new Domain(BASETYPE_APP_WEB_ID);
	}
	
	public static Set<Domain> initDomainSet() {
		Set<Domain> domainSet = new HashSet<Domain>();
		String[] functions = new String[] {
				"newPortal", "newPortalShell",
				"newGate", "newGateApi", "newGateShell",
				"newApp", "newAppWeb"
		};
		for (String function : functions) {
			try {
				ProxyUtil.staticInvoke(Domain.class, function);
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
			}
		}
		
		return domainSet;
	}

	@Override
	protected void init() {
		
		// TODO Auto-generated method stub
		
	}
}
