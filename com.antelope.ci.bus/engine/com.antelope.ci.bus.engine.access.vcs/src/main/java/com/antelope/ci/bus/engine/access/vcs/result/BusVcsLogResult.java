// com.antelope.ci.bus.vcs.result.BusVcsLogResult.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.vcs.result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.antelope.ci.bus.common.DateUtil;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-20		下午6:30:11 
 */
public class BusVcsLogResult extends BusVcsResult {
	protected List<BusVcsLogResultInfo> logList;
	
	public BusVcsLogResult() {
		logList = new ArrayList<BusVcsLogResultInfo>();
	}
	
	public List<BusVcsLogResultInfo> getLogList() {
		return logList;
	}

	public void setLogList(List<BusVcsLogResultInfo> logList) {
		this.logList = logList;
	}

	public void addLog(BusVcsLogResultInfo log) {
		logList.add(log);
	}

	public static class BusVcsLogResultInfo {
		protected String username;
		protected String email;
		protected Date commit_time;
		protected String name;
		protected String message;
		
		public BusVcsLogResultInfo() {
			
		}
		
		public BusVcsLogResultInfo(String username, String email, Date commit_time, 
				String name, String message) {
			this.username = username;
			this.email = email;
			this.commit_time = commit_time;
			this.name = name;
			this.message = message;
		}
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public Date getCommit_time() {
			return commit_time;
		}
		public void setCommit_time(Date commit_time) {
			this.commit_time = commit_time;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public String toString() {
			return "name=" + name + ",username=" + username + ", email=" + email + 
					", commit_time=" + DateUtil.formatTime(commit_time) + "{" + message + "}";
		}
	}
}

