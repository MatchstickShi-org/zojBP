package com.zoj.bp.common.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * @author wangw
 */
public class OrderApprove implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7937493510888925818L;

	private Integer id;
	
	@NotNull
	private Integer orderId;
	
	private Integer designerId;
	
	private Integer claimer;
	
	private Integer approver;
	
	private Integer operate;
	
	private String oprateTime;
	
	@NotNull
	private String remark;

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

	public Integer getClaimer() {
		return claimer;
	}

	public void setClaimer(Integer claimer) {
		this.claimer = claimer;
	}

	public Integer getApprover() {
		return approver;
	}

	public void setApprover(Integer approver) {
		this.approver = approver;
	}

	public Integer getOperate() {
		return operate;
	}

	public void setOperate(Integer operate) {
		this.operate = operate;
	}

	public String getOprateTime() {
		return oprateTime;
	}

	public void setOprateTime(String oprateTime) {
		this.oprateTime = oprateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDesignerId() {
		return designerId;
	}

	public void setDesignerId(Integer designerId) {
		this.designerId = designerId;
	}
}
