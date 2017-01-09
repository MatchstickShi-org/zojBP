package com.zoj.bp.design.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author wangw
 */
public class DesignerVisitApply implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -581658261661806917L;

	private Integer id;
	
	@NotNull
	private Integer orderId;
	
	private Integer designer;
	
	private String designerName;
	
	private String salesmanName;
	
	private Integer approver;
	
	private String approverName;//审核人名称
	
	private Integer status;
	
	private Integer orderStatus;
	
	private Integer notVisitDays;//未回访天数
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	private String name;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getDesigner() {
		return designer;
	}

	public void setDesigner(Integer designer) {
		this.designer = designer;
	}

	public String getDesignerName() {
		return designerName;
	}

	public void setDesignerName(String designerName) {
		this.designerName = designerName;
	}

	public Integer getApprover() {
		return approver;
	}

	public void setApprover(Integer approver) {
		this.approver = approver;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public Integer getNotVisitDays() {
		return notVisitDays;
	}

	public void setNotVisitDays(Integer notVisitDays) {
		this.notVisitDays = notVisitDays;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
}
