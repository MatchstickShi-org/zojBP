package com.zoj.bp.common.model;

import java.io.Serializable;

/**
 * @author wangw
 */
public class OrderApproveFlow implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9009386661981089873L;

	private Integer id;
	
	private Integer status;
	
	private Integer nextStatus;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getNextStatus() {
		return nextStatus;
	}

	public void setNextStatus(Integer nextStatus) {
		this.nextStatus = nextStatus;
	}
}
