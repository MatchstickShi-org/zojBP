package com.zoj.bp.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangw
 */
public class OrderApproveFlow implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7937493510888925818L;

	private Integer id;
	
	private Integer orderId;
	
	private Integer claimer;
	
	private Integer approver;
	
	private Integer oprate;
	
	private Date oprateTime;

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

	public Integer getOprate() {
		return oprate;
	}

	public void setOprate(Integer oprate) {
		this.oprate = oprate;
	}

	public Date getOprateTime() {
		return oprateTime;
	}

	public void setOprateTime(Date oprateTime) {
		this.oprateTime = oprateTime;
	}
}
