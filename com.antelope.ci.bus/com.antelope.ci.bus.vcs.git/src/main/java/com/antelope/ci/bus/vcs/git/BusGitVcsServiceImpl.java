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
import java.util.Date;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.antelope.ci.bus.common.DateUtil;
import com.antelope.ci.bus.common.FILE_TYPE;
import com.antelope.ci.bus.common.FileNode;
import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.vcs.BusVcsService;
import com.antelope.ci.bus.vcs.model.BusVcsAddBranchModel;
import com.antelope.ci.bus.vcs.model.BusVcsAddModel;
import com.antelope.ci.bus.vcs.model.BusVcsAddTagModel;
import com.antelope.ci.bus.vcs.model.BusVcsCatModel;
import com.antelope.ci.bus.vcs.model.BusVcsCheckoutModel;
import com.antelope.ci.bus.vcs.model.BusVcsCommitModel;
import com.antelope.ci.bus.vcs.model.BusVcsContentModel;
import com.antelope.ci.bus.vcs.model.BusVcsDiffModel;
import com.antelope.ci.bus.vcs.model.BusVcsExportModel;
import com.antelope.ci.bus.vcs.model.BusVcsFetchModel;
import com.antelope.ci.bus.vcs.model.BusVcsListModel;
import com.antelope.ci.bus.vcs.model.BusVcsLogModel;
import com.antelope.ci.bus.vcs.model.BusVcsMergeModel;
import com.antelope.ci.bus.vcs.model.BusVcsModel;
import com.antelope.ci.bus.vcs.model.BusVcsModel.AccessType;
import com.antelope.ci.bus.vcs.model.BusVcsMvModel;
import com.antelope.ci.bus.vcs.model.BusVcsPullModel;
import com.antelope.ci.bus.vcs.model.BusVcsPushModel;
import com.antelope.ci.bus.vcs.model.BusVcsRemoteShowModel;
import com.antelope.ci.bus.vcs.model.BusVcsResetModel;
import com.antelope.ci.bus.vcs.model.BusVcsRmModel;
import com.antelope.ci.bus.vcs.model.BusVcsShowModel;
import com.antelope.ci.bus.vcs.model.BusVcsStatusModel;
import com.antelope.ci.bus.vcs.model.BusVcsUpdateModel;
import com.antelope.ci.bus.vcs.model.BusVcsVersionResult;
import com.antelope.ci.bus.vcs.result.BusVcsCatResult;
import com.antelope.ci.bus.vcs.result.BusVcsContentResult;
import com.antelope.ci.bus.vcs.result.BusVcsDiffResult;
import com.antelope.ci.bus.vcs.result.BusVcsListResult;
import com.antelope.ci.bus.vcs.result.BusVcsLogResult;
import com.antelope.ci.bus.vcs.result.BusVcsRemoteShowResult;
import com.antelope.ci.bus.vcs.result.BusVcsResult;
import com.antelope.ci.bus.vcs.result.BusVcsShowResult;
import com.antelope.ci.bus.vcs.result.BusVcsStatusResult;

/**
 * git操作service
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-19 下午4:14:12
 */
public class BusGitVcsServiceImpl implements BusVcsService {
	private BusVcsModel model;

	public BusGitVcsServiceImpl() {
	}

	public BusGitVcsServiceImpl(BusVcsModel model) {
		this.model = model;
	}

	/**
	 * 使用push来验证连通性 (non-Javadoc)
	 * 
	 * @see com.antelope.ci.bus.vcs.BusVcsService#connect(com.antelope.ci.bus.vcs.model.BusVcsModel)
	 */
	@Override
	public BusVcsResult connect(BusVcsModel model) {
		mergeModel(model);
		BusVcsResult result = new BusVcsResult();
		Transport transport = null;
		try {
			URIish uri = createUri(model);
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
	 * 
	 * @see com.antelope.ci.bus.vcs.BusVcsService#clone(com.antelope.ci.bus.vcs.model.BusVcsModel)
	 */
	@Override
	public BusVcsResult clone(BusVcsModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			File directory = new File(model.getReposPath());
			if (directory.exists()) {
				result.setError("not colne : folder exist");
				return result;
			}
			clone(directory, model.getUrl());
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.antelope.ci.bus.vcs.BusVcsService#add(com.antelope.ci.bus.vcs.model.BusVcsAddModel)
	 */
	@Override
	public BusVcsResult add(BusVcsAddModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
			AddCommand command = git.add();
			for (FileNode node : model.getAddList()) {
				command.addFilepattern(node.getPath());
			}
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
	 * 
	 * @see com.antelope.ci.bus.vcs.BusVcsService#commit(com.antelope.ci.bus.vcs.model.BusVcsCommitModel)
	 */
	@Override
	public BusVcsResult commit(BusVcsCommitModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
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
	 * 
	 * @see com.antelope.ci.bus.vcs.BusVcsService#update(com.antelope.ci.bus.vcs.model.BusVcsUpdateModel)
	 */
	@Override
	public BusVcsResult update(BusVcsUpdateModel model) {
		BusVcsResult result = new BusVcsResult();
		result.setException(new CIBusException("", "unsupport update for git"));
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.antelope.ci.bus.vcs.BusVcsService#checkout(com.antelope.ci.bus.vcs.model.BusVcsCheckoutModel)
	 */
	@Override
	public BusVcsResult checkout(BusVcsCheckoutModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
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
	 * 
	 * @see com.antelope.ci.bus.vcs.BusVcsService#export(com.antelope.ci.bus.vcs.model.BusVcsExportModel)
	 */
	@Override
	public BusVcsResult export(BusVcsExportModel model) {
		BusVcsResult result = new BusVcsResult();
		result.setException(new CIBusException("", "unsupport export for git"));
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.antelope.ci.bus.vcs.BusVcsService#rm(com.antelope.ci.bus.vcs.model.BusVcsRmModel)
	 */
	@Override
	public BusVcsResult rm(BusVcsRmModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
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

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.antelope.ci.bus.vcs.BusVcsService#mv(com.antelope.ci.bus.vcs.model.BusVcsMvModel)
	 */
	@Override
	public BusVcsResult mv(BusVcsMvModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
			StoredConfig config = git.getRepository().getConfig();
			if (model.isRebase())
				config.setBoolean(ConfigConstants.CONFIG_BRANCH_SECTION,
						model.getBranch(), ConfigConstants.CONFIG_KEY_REBASE,
						true);
			config.setString(ConfigConstants.CONFIG_BRANCH_SECTION,
					model.getBranch(), model.getOldname(), model.getNewname());
			config.save();
			git.branchRename().setNewName(model.getBranch()).call();
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.antelope.ci.bus.vcs.BusVcsService#list(com.antelope.ci.bus.vcs.model.BusVcsListModel)
	 */
	@Override
	public BusVcsListResult list(BusVcsListModel model) {
		BusVcsListResult result = new BusVcsListResult();
		mergeModel(model);
		try {
			String branch = model.getBranch() == null ? "refs/heads/master"
					: model.getBranch();
			if (model.getAccessType() == AccessType.REMOTE) {
				String today = DateUtil.formatDay(new Date());
				String name = "git_" + today;
				String split = "_";
				FileUtil.delFolderWithDay(name, split, 2);
				File tempDir = FileUtil.genTempFolder(name);
				model.setReposPath(tempDir.getPath());
				clone(tempDir, model.getUrl());
			}
			Git git = createGit(model.getRepository());
			Repository repository = git.getRepository();
			RevWalk walk = new RevWalk(repository);
			Ref ref = repository.getRef(branch);
			ObjectId objId = ref.getObjectId();
			RevCommit revCommit = walk.parseCommit(objId);
			RevTree revTree = revCommit.getTree();

			makeNodeTree(repository, revTree, result);
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	private void makeNodeTree(Repository repository, RevTree revTree,
			BusVcsListResult result) throws Exception {
		ObjectReader reader = repository.newObjectReader();
		TreeWalk tw = new TreeWalk(reader);
		tw.reset(revTree);
		tw.setRecursive(true);
		while (tw.next()) {
			FileNode node = new FileNode();
			node.setType(FILE_TYPE.FILE);
			node.setPath(tw.getPathString());
			result.addNode(node);
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.antelope.ci.bus.vcs.BusVcsService#reset(com.antelope.ci.bus.vcs.model.BusVcsResetModel)
	 */
	@Override
	public BusVcsResult reset(BusVcsResetModel model) {
		BusVcsListResult result = new BusVcsListResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
			ResetType type;
			switch (model.getReset_type()) {
			case 1:
				type = ResetType.SOFT;
				break;
			case 2:
				type = ResetType.MIXED;
				break;
			case 3:
				type = ResetType.MERGE;
				break;
			case 4:
				type = ResetType.MERGE;
				break;
			default:
				type = ResetType.HARD;
				break;
			}

			git.reset().setMode(type).setRef(model.getBranch()).call();
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
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
	public BusVcsRemoteShowResult remote_show(BusVcsRemoteShowModel model) {

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

	private Git createGit(File repos) throws CIBusException {
		try {
			FileRepository db = new FileRepository(repos + File.separator
					+ ".git");
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

	private URIish createUri(BusVcsModel model) throws CIBusException {
		try {
			URIish uri = new URIish(model.getUrl());
			uri = uri.setUser(model.getUsername());
			uri = uri.setPass(model.getPassword());

			return uri;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CIBusException("", e);
		}
	}

	private void clone(File directory, String uri) throws Exception {
		directory.mkdirs();
		CloneCommand command = Git.cloneRepository();
		command.setDirectory(directory);
		command.setURI(uri);
		command.call();
	}

	@Override
	public BusVcsVersionResult getBranchList(BusVcsRmModel model) {

		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public BusVcsVersionResult getTagList(BusVcsRmModel model) {

		// TODO Auto-generated method stub
		return null;

	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.antelope.ci.bus.vcs.BusVcsService#readContent(com.antelope.ci.bus.vcs.model.BusVcsContentModel)
	 */
	@Override
	public BusVcsContentResult readContent(BusVcsContentModel model) {
		BusVcsContentResult result = new BusVcsContentResult();
		mergeModel(model);
		try {
			String branch = model.getBranch() == null ? "refs/heads/master"
					: model.getBranch();
			if (model.getAccessType() == AccessType.REMOTE) {
				String today = DateUtil.formatDay(new Date());
				String name = "git_" + today;
				String split = "_";
				FileUtil.delFolderWithDay(name, split, 2);
				File tempDir = FileUtil.genTempFolder(name);
				model.setReposPath(tempDir.getPath());
				clone(tempDir, model.getUrl());
			}
			Git git = createGit(model.getRepository());
			Repository repository = git.getRepository();
			RevWalk walk = new RevWalk(repository);
			Ref ref = repository.getRef(branch);
			ObjectId objId = ref.getObjectId();
			RevCommit revCommit = walk.parseCommit(objId);
			RevTree revTree = revCommit.getTree();
			TreeWalk treeWalk = TreeWalk.forPath(repository,
					model.getPath(), revTree);
			if (treeWalk != null) {
				ObjectId blobId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(blobId);
				byte[] bytes = loader.getBytes();
				if (bytes != null)
					result.setContent(new String(bytes));
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

}
