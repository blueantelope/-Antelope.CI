// com.antelope.ci.bus.server.service.userstore.FileUserStoreServiceImpl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service.userstore;

import java.util.Map;

import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.model.user.User;
import com.antelope.ci.bus.osgi.BusContext;
import com.antelope.ci.bus.osgi.BusOsgiUtil;
import com.antelope.ci.bus.osgi.Service;
import com.antelope.ci.bus.server.service.CommonServerService;
import com.antelope.ci.bus.server.service.UserStoreServerService;


/**
 * file store for user
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-8		下午12:01:44 
 */
@Service(name=UserStoreServerService.NAME)
public class FileUserStoreServiceImpl extends CommonServerService implements UserStoreServerService {
	public FileUserStoreServiceImpl() {
		super();
		init();
	}
	
	private void init() {
		PasswordHelper.getHelper().init();
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.service.UserStoreServerService#getUserMap()
	 */
	@Override
	public Map<String, User> getUserMap() {
		return PasswordHelper.getHelper().userMap;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.service.UserStoreServerService#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String username) {
		return PasswordHelper.getHelper().userMap.get(username);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.IService#publish(org.osgi.framework.BundleContext)
	 * @Deprecated replace by {@link #publish(BusContext context)}
	 */
	@Deprecated
	@Override
	public boolean publish(BundleContext m_context) throws CIBusException {
		BusOsgiUtil.publishService(m_context, this, NAME);
		return true;
	}

	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.IService#publish(com.antelope.ci.bus.osgi.BusContext)
	 */
	@Override
	public boolean publish(BusContext context) throws CIBusException {
		BusOsgiUtil.publishService(context.getBundleContext(), this, NAME);
		return true;
	}
}
