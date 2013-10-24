// com.antelope.ci.bus.vcs.git.test.TestBaseGit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.vcs.git.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import junit.framework.TestCase;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;

import com.antelope.ci.bus.vcs.git.BusGitVcsServiceImpl;
import com.antelope.ci.bus.vcs.model.BusVcsModel;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-19		下午10:12:13 
 */
public abstract class TestBaseGit extends TestCase {
	protected static final String root = TestBaseGit.class.getResource("/").getFile() + "test_git";
	protected static final String repos = root + File.separator + "repos";
	protected static final String repos_git = repos + File.separator + ".git";
	protected static final String clone = root +  File.separator + "clone";
	protected static final String test_antelopeCI = root +  File.separator + "testAntelopeCI";
//	protected static final String url = "https://github.com/testantelope/testAntelopeCI.git";
//	protected static final String username = "testantelope";
//	protected static final String password = "54antelope";
	protected static final String url = "https://code.csdn.net/blueantelope2008/test_antelopeci.git";
	protected static final String username = "blueantelope@163.com";
	protected static final String password = "54antelope";
	protected File root_file =  new File(root);
	protected Git remote_local_git;
	protected File clone_file = new File(clone);
	protected FileRepository file_repos;
	protected BusVcsModel model;
	protected BusGitVcsServiceImpl gitService;
	
	@Override
	protected void setUp() throws Exception {
		if (!root_file.exists())
			root_file.mkdir();
		
		File repos_dir = new File(repos);
		if (!repos_dir.exists()) {
			repos_dir.mkdir();
			file_repos = new FileRepository(new File(repos_git));
			file_repos.create();
		} else {
			file_repos = new FileRepository(new File(repos_git));
		}
		remote_local_git = new Git(file_repos);
		if (!clone_file.exists())
			clone_file.mkdir();
		
		model = new BusVcsModel();
		model.setUrl(url);
		model.setUsername(username);
		model.setPassword(password);
		gitService = new BusGitVcsServiceImpl();
		init();
	}

	protected void write_repos(String name, String contents) throws Exception {
		File f = new File(file_repos.getWorkTree(), name);
		Writer w = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
		try {
			w.write(contents);
		} finally {
			w.close();
		}
		remote_local_git.add().addFilepattern(name).call();
		remote_local_git.commit().setMessage("Initial commit").call();
		remote_local_git.tag().setName("tag-initial").setMessage("Tag initial").call();
	}
	
	protected abstract void init() throws Exception;
}

