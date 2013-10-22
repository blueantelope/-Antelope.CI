// com.antelope.ci.bus.vcs.model.BusVcsResultModel.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.vcs.result;

import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * result for VCS operating
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-20		下午6:13:44 
 */
public class BusVcsResult {
	// 
	public enum VCS_RESULT {
		SUCCESS(0, "success"),
		FAILTURE(1, "failture"),
		EXCEPTION(2, "exception"),
		ERROR(3, "error");
		
		private int code;
		private String name;
		private VCS_RESULT(int code, String name) {
			this.code = code;
			this.name = name;
		}
		public int getCode() {
			return code;
		}
		public String getName() {
			return name;
		}
		public static VCS_RESULT toResult(String name) throws CIBusException {
			if (name != null && name.length() > 0) {
				for (VCS_RESULT result : VCS_RESULT.values()) {
					if (result.name.equalsIgnoreCase(name.trim()))
						return result;
				}
			}
			
			throw new CIBusException("", "undefined result of VCS opreation");
		}
	}
	
	private VCS_RESULT result;
	private String message;
	private List<BusVcsResult> problemList;
	
	public BusVcsResult() {
		this.result = VCS_RESULT.SUCCESS;
		this.message = "success";
		problemList = new ArrayList<BusVcsResult>();
	}
	
	public BusVcsResult(VCS_RESULT result, String message) {
		this.result = result;
		this.message = message;
	}
	
	// getter and setter
	public VCS_RESULT getResult() {
		return result;
	}
	public void setResult(VCS_RESULT result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<BusVcsResult> getProblemList() {
		return problemList;
	}
	public void setProblemList(List<BusVcsResult> problemList) {
		this.problemList = problemList;
	}
	public void addProblem(BusVcsResult problem) {
		problemList.add(problem);
	}
	public void setException(String message) {
		this.result = VCS_RESULT.EXCEPTION;
		this.message = message;
	}
	public void setException(Exception e) {
		this.result = VCS_RESULT.EXCEPTION;
		this.message = e.getMessage();
	}
	public void setError(String message) {
		this.result = VCS_RESULT.ERROR;
		this.message = message;
	}
}

