// com.antelope.ci.bus.vcs.BusVcsService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.vcs;

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

	public BusVcsResult login(BusVcsModel model);

	public BusVcsResult add(BusVcsAddModel model);
	
	public BusVcsResult commit(BusVcsCommitModel model);
	
	public BusVcsResult update(BusVcsUpdateModel model);
	
	public BusVcsResult checkout(BusVcsCheckoutModel model);
	
	public BusVcsResult export(BusVcsExportModel model);
	
	public BusVcsResult rm(BusVcsRmModel model);
	
	public BusVcsResult mv(BusVcsMvModel model);
	
	public BusVcsListResult list(BusVcsListModel model);
	
	public BusVcsResult reset(BusVcsResetModel model);
	
	public BusVcsDiffResult diff(BusVcsDiffModel model);
	
	public BusVcsLogResult log(BusVcsLogModel model);
	
	public BusVcsStatusResult status(BusVcsStatusModel model);
	
	public BusVcsShowResult show(BusVcsShowModel model);
	
	public BusVcsCatResult cat(BusVcsCatModel model);
	
	public BusVcsRemoteShowResult remote_show(BusVcsRemoteShowModel model);
	
	public BusVcsResult fetch(BusVcsFetchModel model);
	
	public BusVcsResult pull(BusVcsPullModel model);
	
	public BusVcsResult push(BusVcsPushModel model);
	
	public BusVcsResult addBranch(BusVcsAddBranchModel model);
	
	public BusVcsResult merge(BusVcsMergeModel model);
	
	public BusVcsResult addTag(BusVcsAddTagModel model);
}

