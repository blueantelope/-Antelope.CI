// com.antelope.ci.bus.vcs.git.BusGitVcsServiceImpl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.vcs.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.URIish;

import com.antelope.ci.bus.common.FileNode;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.vcs.BusVcsService;
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
 * git操作service
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-19		下午4:14:12 
 */
public class BusGitVcsServiceImpl implements BusVcsService {
	private BusVcsModel model;
	
	public BusGitVcsServiceImpl() {
	}
	
	public BusGitVcsServiceImpl(BusVcsModel model) {
		this.model = model;
	}

	/**
	 * 使用push来验证连通性
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.vcs.BusVcsService#connect(com.antelope.ci.bus.vcs.model.BusVcsModel)
	 */
	@Override
	public BusVcsResult connect(BusVcsModel model) throws CIBusException {
		mergeModel(model);
		BusVcsResult result = new BusVcsResult();
		Transport transport = null;
		try {
			URIish uri = new URIish(model.getUrl());
			uri = uri.setUser(model.getUsername());
			uri = uri.setPass(model.getPassword());
			transport = Transport.open(uri);
			transport.setTimeout(5);
			transport.openPush();
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		} finally {
			if (transport != null)
				transport.close();
			return result;
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.vcs.BusVcsService#add(com.antelope.ci.bus.vcs.model.BusVcsAddModel)
	 */
	@Override
	public BusVcsResult add(BusVcsAddModel model) throws CIBusException {
		mergeModel(model);
		BusVcsResult result = new BusVcsResult();
		Git git = createGit(model.getRepository());
		AddCommand command = git.add();
		for (FileNode node : model.getAddList()) {
			command.addFilepattern(node.getPath());
		}
		try {
			command.call();
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.vcs.BusVcsService#commit(com.antelope.ci.bus.vcs.model.BusVcsCommitModel)
	 */
	@Override
	public BusVcsResult commit(BusVcsCommitModel model) throws CIBusException {
		mergeModel(model);
		BusVcsResult result = new BusVcsResult();
		Git git = createGit(model.getRepository());
		try {
			git.commit().setMessage("create file").call();
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		} 
		
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.vcs.BusVcsService#update(com.antelope.ci.bus.vcs.model.BusVcsUpdateModel)
	 */
	@Override
	public BusVcsResult update(BusVcsUpdateModel model) throws CIBusException {
		BusVcsResult result = new BusVcsResult();
		result.setException(new CIBusException("", "unsupport update for git"));
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.vcs.BusVcsService#checkout(com.antelope.ci.bus.vcs.model.BusVcsCheckoutModel)
	 */
	@Override
	public BusVcsResult checkout(BusVcsCheckoutModel model) throws CIBusException {
		mergeModel(model);
		BusVcsResult result = new BusVcsResult();
		Git git = createGit(model.getRepository());
		try {
			CheckoutCommand command = git.checkout();
			if (model.isCreate())
				command.setCreateBranch(true);
			command.setName(model.getName());
			command.call();
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		} 
		
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.vcs.BusVcsService#export(com.antelope.ci.bus.vcs.model.BusVcsExportModel)
	 */
	@Override
	public BusVcsResult export(BusVcsExportModel model) throws CIBusException {
		BusVcsResult result = new BusVcsResult();
		result.setException(new CIBusException("", "unsupport export for git"));
		return result;
	}


	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.vcs.BusVcsService#rm(com.antelope.ci.bus.vcs.model.BusVcsRmModel)
	 */
	@Override
	public BusVcsResult rm(BusVcsRmModel model) throws CIBusException {
		mergeModel(model);
		BusVcsResult result = new BusVcsResult();
		Git git = createGit(model.getRepository());
		try {
			RmCommand command = git.rm();
			if (model.isCached())
				command.setCached(true);
			for (FileNode node : model.getRmList()) {
				command.addFilepattern(node.getPath());
			}
			command.call();
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		} 
		
		return result;
	}


	@Override
	public BusVcsResult mv(BusVcsMvModel model) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsListResult list(BusVcsListModel model) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsResult reset(BusVcsResetModel model) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsDiffResult diff(BusVcsDiffModel model) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsLogResult log(BusVcsLogModel model) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsStatusResult status(BusVcsStatusModel model)
			throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsShowResult show(BusVcsShowModel model) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsCatResult cat(BusVcsCatModel model) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsRemoteShowResult remote_show(BusVcsRemoteShowModel model)
			throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsResult fetch(BusVcsFetchModel model) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsResult pull(BusVcsPullModel model) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsResult push(BusVcsPushModel model) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsResult addBranch(BusVcsAddBranchModel model)
			throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsResult merge(BusVcsMergeModel model) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public BusVcsResult addTag(BusVcsAddTagModel model) throws CIBusException {
		
		// TODO Auto-generated method stub
		return null;
		
	}
	
	private Git createGit(File repos) throws CIBusException {
		try {
			FileRepository db = new FileRepository(repos);
			return new Git(db);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
		
	}
	
	private void mergeModel(BusVcsModel pmodle) {
		if (this.model != null) {
			pmodle.setInfo(model);
		}
	}
}

