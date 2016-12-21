package com.zoj.bp.common.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * @author wangw
 */
public class Order implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1941948674701203482L;

	private Integer id;
	
	@NotNull
	private Integer infoerId;
	
	private String infoerName;
	
	private Integer salesmanId;
	
	private String salesmanName;
	
	private Integer stylistId;
	
	private String stylistName;
	
	private String projectName;
	
	private String projectAddr;
	
	private String insertTime;
	
	private Integer status;

	@NotNull
	private String name;
	
	private String orgAddr;
	
	@NotNull
	private String tel1;
	
	private String tel2;
	
	private String tel3;
	
	private String tel4;
	
	private String tel5;

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

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime.substring(0,19);
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getInfoerName() {
		return infoerName;
	}

	public void setInfoerName(String infoerName) {
		this.infoerName = infoerName;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public String getStylistName() {
		return stylistName;
	}

	public void setStylistName(String stylistName) {
		this.stylistName = stylistName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getOrgAddr() {
		return orgAddr;
	}

	public void setOrgAddr(String orgAddr) {
		this.orgAddr = orgAddr;
	}

	public String getTel1() {
		return tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getTel3() {
		return tel3;
	}

	public void setTel3(String tel3) {
		this.tel3 = tel3;
	}

	public String getTel4() {
		return tel4;
	}

	public void setTel4(String tel4) {
		this.tel4 = tel4;
	}

	public String getTel5() {
		return tel5;
	}

	public void setTel5(String tel5) {
		this.tel5 = tel5;
	}
	
}
