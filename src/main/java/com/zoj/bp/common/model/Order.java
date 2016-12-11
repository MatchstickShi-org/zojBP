package com.zoj.bp.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangw
 */
public class Order implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1941948674701203482L;

	private Integer id;
	
	private Integer infoerId;
	
	private Integer salesmanId;
	
	private Integer stylistId;
	
	private String projectName;
	
	private String projectAddr;
	
	private Date insertTime;
	
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getInfoerId() {
		return infoerId;
	}

	public void setInfoerId(Integer infoerId) {
		this.infoerId = infoerId;
	}

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public Integer getStylistId() {
		return stylistId;
	}

	public void setStylistId(Integer stylistId) {
		this.stylistId = stylistId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectAddr() {
		return projectAddr;
	}

	public void setProjectAddr(String projectAddr) {
		this.projectAddr = projectAddr;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
