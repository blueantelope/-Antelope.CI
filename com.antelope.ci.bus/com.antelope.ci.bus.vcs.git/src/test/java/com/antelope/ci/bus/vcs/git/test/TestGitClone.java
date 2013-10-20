// com.antelope.ci.bus.vcs.git.test.TestGitClone.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.vcs.git.test;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.junit.Test;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-19		下午10:22:41 
 */
public class TestGitClone extends TestBaseGit {
	@Override
	protected void init() throws Exception {
		write_repos("clone.txt", "test for clone");
	}
	
	@Test
	public void test() throws InvalidRemoteException, TransportException, GitAPIException {
		CloneCommand command = Git.cloneRepository();
		command.setDirectory(clone_file);
		command.setURI("file://" + remote_local_git.getRepository().getWorkTree().getPath());
		command.call();
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestGitClone.class);
	}

}

