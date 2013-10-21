// com.antelope.ci.bus.vcs.BusVcsService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.vcs;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.vcs.model.BusVcsAddBranchModel;
import com.antelope.ci.bus.vcs.model.BusVcsAddModel;
import com.antelope.ci.bus.vcs.model.BusVcsAddTagModel;
import com.antelope.ci.bus.vcs.model.BusVcsCatModel;
import com.antelope.ci.bus.vcs.model.BusVcsCheckoutModel;
import com.antelope.ci.bus.vcs.model.BusVcsCommitModel;
import com.antelope.ci.bus.vcs.model.BusVcsDiffModel;
import com.antelope.ci.bus.vcs.model.BusVcsExportModel;
import com.antelope.ci.bus.vcs.model.BusVcsFetchModel;
import com.antelope.ci.bus.vcs.model.BusVcsListModel;
import com.antelope.ci.bus.vcs.model.BusVcsLogModel;
import com.antelope.ci.bus.vcs.model.BusVcsMergeModel;
import com.antelope.ci.bus.vcs.model.BusVcsModel;
import com.antelope.ci.bus.vcs.model.BusVcsMvModel;
import com.antelope.ci.bus.vcs.model.BusVcsPullModel;
import com.antelope.ci.bus.vcs.model.BusVcsPushModel;
import com.antelope.ci.bus.vcs.model.BusVcsRemoteShowModel;
import com.antelope.ci.bus.vcs.model.BusVcsResetModel;
import com.antelope.ci.bus.vcs.model.BusVcsRmModel;
import com.antelope.ci.bus.vcs.model.BusVcsShowModel;
import com.antelope.ci.bus.vcs.model.BusVcsStatusModel;
import com.antelope.ci.bus.vcs.model.BusVcsUpdateModel;
import com.antelope.ci.bus.vcs.result.BusVcsCatResult;
import com.antelope.ci.bus.vcs.result.BusVcsDiffResult;
import com.antelope.ci.bus.vcs.result.BusVcsListResult;
import com.antelope.ci.bus.vcs.result.BusVcsLogResult;
import com.antelope.ci.bus.vcs.result.BusVcsRemoteShowResult;
import com.antelope.ci.bus.vcs.result.BusVcsResult;
import com.antelope.ci.bus.vcs.result.BusVcsShowResult;
import com.antelope.ci.bus.vcs.result.BusVcsStatusResult;


/**
 * VCS servcie
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-19		下午3:47:44 
 */
public interface BusVcsService {

	public BusVcsResult login(BusVcsModel model) throws CIBusException ;

	public BusVcsResult add(BusVcsAddModel model) throws CIBusException ;
	
	public BusVcsResult commit(BusVcsCommitModel model) throws CIBusException;
	
	public BusVcsResult update(BusVcsUpdateModel model) throws CIBusException;
	
	public BusVcsResult checkout(BusVcsCheckoutModel model) throws CIBusException;
	
	public BusVcsResult export(BusVcsExportModel model) throws CIBusException;
	
	public BusVcsResult rm(BusVcsRmModel model) throws CIBusException;
	
	public BusVcsResult mv(BusVcsMvModel model) throws CIBusException;
	
	public BusVcsListResult list(BusVcsListModel model) throws CIBusException;
	
	public BusVcsResult reset(BusVcsResetModel model) throws CIBusException;
	
	public BusVcsDiffResult diff(BusVcsDiffModel model) throws CIBusException;
	
	public BusVcsLogResult log(BusVcsLogModel model) throws CIBusException;
	
	public BusVcsStatusResult status(BusVcsStatusModel model) throws CIBusException;
	
	public BusVcsShowResult show(BusVcsShowModel model) throws CIBusException;
	
	public BusVcsCatResult cat(BusVcsCatModel model) throws CIBusException;
	
	public BusVcsRemoteShowResult remote_show(BusVcsRemoteShowModel model) throws CIBusException;
	
	public BusVcsResult fetch(BusVcsFetchModel model) throws CIBusException;
	
	public BusVcsResult pull(BusVcsPullModel model) throws CIBusException;
	
	public BusVcsResult push(BusVcsPushModel model) throws CIBusException;
	
	public BusVcsResult addBranch(BusVcsAddBranchModel model) throws CIBusException;
	
	public BusVcsResult merge(BusVcsMergeModel model) throws CIBusException;
	
	public BusVcsResult addTag(BusVcsAddTagModel model) throws CIBusException;
}

