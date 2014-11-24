// com.antelope.ci.bus.vcs.git.BusGitVcsServiceImpl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.engine.access.vcs.git;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.DeleteBranchCommand;
import org.eclipse.jgit.api.DeleteTagCommand;
import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RenameBranchCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.TagCommand;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.TagOpt;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import com.antelope.ci.bus.common.DateUtil;
import com.antelope.ci.bus.common.FILE_TYPE;
import com.antelope.ci.bus.common.FileNode;
import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
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
import com.antelope.ci.bus.engine.model.vcs.input.BusVcsModel.AccessType;
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
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsVersionResult;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsLogResult.BusVcsLogResultInfo;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsResult;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsShowResult;
import com.antelope.ci.bus.engine.model.vcs.output.BusVcsStatusResult;

/**
 * git操作service
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-19 下午4:14:12
 */
public class BusGitVcsServiceImpl implements BusVcsAccess {
	private BusVcsModel model;

	public BusGitVcsServiceImpl() {
	}

	public BusGitVcsServiceImpl(BusVcsModel model) {
		this.model = model;
	}

	/**
	 * 使用push来验证连通性 (non-Javadoc)
	 * 
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#connect(com.antelope.ci.bus.engine.model.vcs.input.BusVcsModel)
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
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#clone(com.antelope.ci.bus.engine.model.vcs.input.BusVcsModel)
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
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#add(com.antelope.ci.bus.engine.model.vcs.input.BusVcsAddModel)
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
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#commit(com.antelope.ci.bus.engine.model.vcs.input.BusVcsCommitModel)
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
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#update(com.antelope.ci.bus.engine.model.vcs.input.BusVcsUpdateModel)
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
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#checkout(com.antelope.ci.bus.engine.model.vcs.input.BusVcsCheckoutModel)
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
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#export(com.antelope.ci.bus.engine.model.vcs.input.BusVcsExportModel)
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
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#rm(com.antelope.ci.bus.engine.model.vcs.input.BusVcsRmModel)
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
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#mv(com.antelope.ci.bus.engine.model.vcs.input.BusVcsMvModel)
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
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#list(com.antelope.ci.bus.engine.model.vcs.input.BusVcsListModel)
	 */
	@Override
	public BusVcsListResult list(BusVcsListModel model) {
		BusVcsListResult result = new BusVcsListResult();
		mergeModel(model);
		try {
			String branch = model.getBranch() == null ? "refs/heads/master"
					: model.getBranch();
			if (model.getAccessType() == AccessType.REMOTE) {
				cloneTemp(model.getUrl());
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
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#reset(com.antelope.ci.bus.engine.model.vcs.input.BusVcsResetModel)
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

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#diff(com.antelope.ci.bus.engine.model.vcs.input.BusVcsDiffModel)
	 */
	@Override
	public BusVcsDiffResult diff(BusVcsDiffModel model) {
		BusVcsDiffResult result = new BusVcsDiffResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
			if (model.getPathList().isEmpty()) {
				result.setContent(diff(git, model, null));
			} else {
				StringBuffer sBuff = new StringBuffer();
				for (String path : model.getPathList()) {
					sBuff.append(diff(git, model, path));
				}
				result.setContent(sBuff.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}
	
	private String diff(Git git, BusVcsDiffModel model, String path) throws CIBusException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			DiffCommand command = git.diff();
			command.setOutputStream(out);
			if (model.isCached())
				command.setCached(true);
			if (model.getOldTree() != null && model.getOldTree().length() > 0) {
				command.setOldTree(getTreeIterator(git.getRepository(), model.getOldTree()));
			}
			if (model.getNewTree() != null && model.getNewTree().length() > 0) {
				command.setNewTree(getTreeIterator(git.getRepository(), model.getNewTree()));
			}
			if (model.getSrcPrefix() != null && model.getSrcPrefix().length() > 0) {
				command.setSourcePrefix(model.getSrcPrefix());
			}
			if (model.getDestPrefix() != null && model.getDestPrefix().length() > 0) {
				command.setDestinationPrefix(model.getDestPrefix());
			}
			if (path != null) {
				command.setPathFilter(PathFilter.create(path));
			}
			command.call();
			String s = out.toString();
			out.close();
			return s;
		} catch (Exception e) {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
				}
			}
			throw new CIBusException("", e);
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#log(com.antelope.ci.bus.engine.model.vcs.input.BusVcsLogModel)
	 */
	@Override
	public BusVcsLogResult log(BusVcsLogModel model) {
		BusVcsLogResult result = new BusVcsLogResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
			
			if (model.getTagName() != null && model.getTagName().length() > 0) {
				TagCommand tagCmd = git.tag();
				tagCmd.setName(model.getTagName());
				tagCmd.call();
			}
			Iterator<RevCommit> logIter = git.log().all().call().iterator();
			while (logIter.hasNext()) {
				RevCommit rev = logIter.next();
				PersonIdent ident = rev.getAuthorIdent();
				String username = ident.getName();
				String email = ident.getEmailAddress();
				Date commit_time = new Date(rev.getCommitTime()*1000);
				String name = rev.getName();
				String message = rev.getFullMessage();
				BusVcsLogResultInfo log = new BusVcsLogResultInfo(
						username, email, commit_time, name, message
						);
				result.addLog(log);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#status(com.antelope.ci.bus.engine.model.vcs.input.BusVcsStatusModel)
	 */
	@Override
	public BusVcsStatusResult status(BusVcsStatusModel model) {
		BusVcsStatusResult result = new BusVcsStatusResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
			StatusCommand command = git.status();
			Status status = command.call();
			result.getAddList().addAll(status.getAdded());
			result.getDeleteList().addAll(status.getRemoved());
			result.getChangeList().addAll(status.getChanged());
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#show(com.antelope.ci.bus.engine.model.vcs.input.BusVcsShowModel)
	 */
	@Override
	public BusVcsShowResult show(BusVcsShowModel model) {
		BusVcsShowResult result = new BusVcsShowResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#cat(com.antelope.ci.bus.engine.model.vcs.input.BusVcsCatModel)
	 */
	@Override
	public BusVcsCatResult cat(BusVcsCatModel model) {
		BusVcsCatResult result = new BusVcsCatResult();
		mergeModel(model);
		try {
			String branch = model.getBranch() == null ? "refs/heads/master"
					: model.getBranch();
			if (model.getAccessType() == AccessType.REMOTE) {
				cloneTemp(model.getUrl());
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

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#fetch(com.antelope.ci.bus.engine.model.vcs.input.BusVcsFetchModel)
	 */
	@Override
	public BusVcsResult fetch(BusVcsFetchModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
			createRemoteConfig(git.getRepository(), model);
			RefSpec spec = new RefSpec(model.convertSpec());
			git.fetch().setRemote("origin").setRefSpecs(spec).setTagOpt(TagOpt.AUTO_FOLLOW).call();
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#pull(com.antelope.ci.bus.engine.model.vcs.input.BusVcsPullModel)
	 */
	@Override
	public BusVcsResult pull(BusVcsPullModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
			createRemoteConfig(git.getRepository(), model);
			git.pull().call();
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#push(com.antelope.ci.bus.engine.model.vcs.input.BusVcsPushModel)
	 */
	@Override
	public BusVcsResult push(BusVcsPushModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
			push(git, model);
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#addBranch(com.antelope.ci.bus.engine.model.vcs.input.BusVcsAddBranchModel)
	 */
	@Override
	public BusVcsResult addBranch(BusVcsAddBranchModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			Git git = createGit(model.getRepository());
			CreateBranchCommand command = git.branchCreate();
			command.setName(model.getName());
			command.setForce(true);
			command.setStartPoint(model.getFromName());
			command.setUpstreamMode(null);
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
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#merge(com.antelope.ci.bus.engine.model.vcs.input.BusVcsMergeModel)
	 */
	@Override
	public BusVcsResult merge(BusVcsMergeModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			if (model.getAccessType() == AccessType.REMOTE) {
				cloneTemp(model.getUrl());
			}
			Git git = createGit(model.getRepository());
			MergeCommand command = git.merge();
			Ref from_tag = git.getRepository().getRef(model.getFromName());
			command.include(from_tag);
			command.setStrategy(MergeStrategy.RESOLVE);
			command.call();
			push(git, model);
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#addTag(com.antelope.ci.bus.engine.model.vcs.input.BusVcsAddTagModel)
	 */
	@Override
	public BusVcsResult addTag(BusVcsAddTagModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			if (model.getAccessType() == AccessType.REMOTE) {
				cloneTemp(model.getUrl());
			}
			Git git = createGit(model.getRepository());
			TagCommand command = git.tag();
			command.setName(model.getName());
			command.call();
			push(git, model);
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}
	

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#getBranchList(com.antelope.ci.bus.engine.model.vcs.input.BusVcsBranchModel)
	 */
	@Override
	public BusVcsVersionResult getBranchList(BusVcsBranchModel model) {
		BusVcsVersionResult result = new BusVcsVersionResult();
		mergeModel(model);
		try {
			if (model.getAccessType() == AccessType.REMOTE) {
				cloneTemp(model.getUrl());
			}
			Git git = createGit(model.getRepository());
			List<Ref> refList = git.branchList().setListMode(ListMode.ALL).call();
			for (Ref ref : refList) {
				result.addVersion(ref.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#getTagList(com.antelope.ci.bus.engine.model.vcs.input.BusVcsTagModel)
	 */
	@Override
	public BusVcsVersionResult getTagList(BusVcsTagModel model) {
		BusVcsVersionResult result = new BusVcsVersionResult();
		mergeModel(model);
		try {
			if (model.getAccessType() == AccessType.REMOTE) {
				cloneTemp(model.getUrl());
			}
			Git git = createGit(model.getRepository());
			List<Ref> refList = git.tagList().call();
			for (Ref ref : refList) {
				result.addVersion(ref.getName());
			}
			push(git, model);
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#deleteBranch(com.antelope.ci.bus.engine.model.vcs.input.BusVcsDeleteBranchModel)
	 */
	@Override
	public BusVcsResult deleteBranch(BusVcsDeleteBranchModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			if (model.getAccessType() == AccessType.REMOTE) {
				cloneTemp(model.getUrl());
			}
			Git git = createGit(model.getRepository());
			DeleteBranchCommand command = git.branchDelete();
			command.setBranchNames(model.getNameList().toArray(new String[model.getNameList().size()]));
			command.call();
			push(git, model);
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#deleteTag(com.antelope.ci.bus.engine.model.vcs.input.BusVcsDeleteTagModel)
	 */
	@Override
	public BusVcsResult deleteTag(BusVcsDeleteTagModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			if (model.getAccessType() == AccessType.REMOTE) {
				cloneTemp(model.getUrl());
			}
			Git git = createGit(model.getRepository());
			DeleteTagCommand command = git.tagDelete();
			command.setTags(model.getNameList().toArray(new String[model.getNameList().size()]));
			command.call();
			push(git, model);
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#renameBranch(com.antelope.ci.bus.engine.model.vcs.input.BusVcsRenameBranchModel)
	 */
	@Override
	public BusVcsResult renameBranch(BusVcsRenameBranchModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			if (model.getAccessType() == AccessType.REMOTE) {
				cloneTemp(model.getUrl());
			}
			Git git = createGit(model.getRepository());
			RenameBranchCommand command = git.branchRename();
			command.setOldName(model.getOldname());
			command.setNewName(model.getNewname());
			command.call();
			push(git, model);
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.engine.access.vcs.BusVcsAccess#renameTag(com.antelope.ci.bus.engine.model.vcs.input.BusVcsRenameTagModel)
	 */
	@Override
	public BusVcsResult renameTag(BusVcsRenameTagModel model) {
		BusVcsResult result = new BusVcsResult();
		mergeModel(model);
		try {
			if (model.getAccessType() == AccessType.REMOTE) {
				cloneTemp(model.getUrl());
			}
			Git git = createGit(model.getRepository());
			TagCommand add_cmd = git.tag();
			add_cmd.setName(model.getNewname());
			add_cmd.call();
			DeleteTagCommand del_cmd = git.tagDelete();
			del_cmd.setTags(model.getNewname());
			del_cmd.call();
			push(git, model);
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}

		return result;
	}

	private void cloneTemp(String url) throws Exception {
		String today = DateUtil.formatDay(new Date());
		String name = "git_" + today;
		String split = "_";
		FileUtil.delFolderWithDay(name, split, 2);
		File tempDir = FileUtil.genTempFolder(name);
		model.setReposPath(tempDir.getPath());
		clone(tempDir, url);
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
	
	private AbstractTreeIterator getTreeIterator(Repository repo, String name) throws IOException {
		final ObjectId id = repo.resolve(name);
		if (id == null)
			throw new IllegalArgumentException(name);
		final CanonicalTreeParser p = new CanonicalTreeParser();
		final ObjectReader or = repo.newObjectReader();
		try {
			p.reset(or, new RevWalk(repo).parseTree(id));
			return p;
		} finally {
			or.release();
		}
	}
	
	private void createRemoteConfig(Repository repo, BusVcsModel model) throws Exception {
		StoredConfig config = repo.getConfig();
		RemoteConfig remoteConfig = new RemoteConfig(config, "origin");
		URIish uri = new URIish(model.getUrl());
		remoteConfig.addURI(uri);
		remoteConfig.addPushURI(uri);
		remoteConfig.update(config);
		config.save();
	}
	
	private void push(Git git, BusVcsModel model) throws Exception {
		createRemoteConfig(git.getRepository(), model);
		RefSpec spec = new RefSpec(model.convertSpec());
		PushCommand command = git.push();
		command.setRefSpecs(spec);
		switch (model.getAuthType()) {
			case PASSWORD:
				command.setCredentialsProvider(
						new UsernamePasswordCredentialsProvider(model.getUsername(), model.getPassword()));
				break;
			case KEY:
				
				
		}
		command.call();
	}
}
