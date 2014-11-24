// com.antelope.ci.bus.vcs.BusVcsService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs;

import com.antelope.ci.bus.engine.model.vcs.input.BusVcsAddBranchModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsAddModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsAddTagModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsBranchModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsCatModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsCheckoutModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsCommitModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsDeleteBranchModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsDeleteTagModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsDiffModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsExportModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsFetchModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsListModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsLogModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsMergeModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsMvModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsPullModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsPushModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsRenameBranchModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsRenameTagModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsResetModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsRmModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsShowModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsStatusModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsTagModel;
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsUpdateModel;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsCatResult;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsDiffResult;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsListResult;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsLogResult;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsResult;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsShowResult;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsStatusResult;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsVersionResult;


/**
 * VCS servcie
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-19		下午3:47:44 
 */
public interface BusVcsAccess {

	public BusVcsResult connect(BusVcsModel model);
	
	public BusVcsResult clone(BusVcsModel model);

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
	
	public BusVcsResult fetch(BusVcsFetchModel model);
	
	public BusVcsResult pull(BusVcsPullModel model);
	
	public BusVcsResult push(BusVcsPushModel model);
	
	public BusVcsResult addBranch(BusVcsAddBranchModel model);
	
	public BusVcsResult merge(BusVcsMergeModel model);
	
	public BusVcsResult addTag(BusVcsAddTagModel model);
	
	public BusVcsVersionResult getBranchList(BusVcsBranchModel model);
	
	public BusVcsVersionResult getTagList(BusVcsTagModel model);
	
	public BusVcsResult deleteBranch(BusVcsDeleteBranchModel model);
	
	public BusVcsResult deleteTag(BusVcsDeleteTagModel model);
	
	public BusVcsResult renameBranch(BusVcsRenameBranchModel model);
	
	public BusVcsResult renameTag(BusVcsRenameTagModel model);
}

