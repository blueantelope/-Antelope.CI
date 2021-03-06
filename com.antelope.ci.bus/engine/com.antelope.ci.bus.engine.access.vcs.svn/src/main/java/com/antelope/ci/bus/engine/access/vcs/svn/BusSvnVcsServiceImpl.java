// com.antelope.ci.bus.vcs.svn.BusSvnVcsServiceImpl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.svn;

import com.antelope.ci.bus.engine.access.vcs.BusVcsAccess;
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
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-20		下午10:14:41 
 */
public class BusSvnVcsServiceImpl implements BusVcsAccess {

	@Override
	public BusVcsResult connect(BusVcsModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult clone(BusVcsModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult add(BusVcsAddModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult commit(BusVcsCommitModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult update(BusVcsUpdateModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult checkout(BusVcsCheckoutModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult export(BusVcsExportModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult rm(BusVcsRmModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult mv(BusVcsMvModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsListResult list(BusVcsListModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult reset(BusVcsResetModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsDiffResult diff(BusVcsDiffModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsLogResult log(BusVcsLogModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsStatusResult status(BusVcsStatusModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsShowResult show(BusVcsShowModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsCatResult cat(BusVcsCatModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult fetch(BusVcsFetchModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult pull(BusVcsPullModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult push(BusVcsPushModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult addBranch(BusVcsAddBranchModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult merge(BusVcsMergeModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult addTag(BusVcsAddTagModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsVersionResult getBranchList(BusVcsBranchModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsVersionResult getTagList(BusVcsTagModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult deleteBranch(BusVcsDeleteBranchModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult deleteTag(BusVcsDeleteTagModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult renameBranch(BusVcsRenameBranchModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public BusVcsResult renameTag(BusVcsRenameTagModel model) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

}

