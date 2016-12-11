/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangw
 */
public class InfoCost implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3426935117405508997L;

	/**id INT(20) not null AUTO_INCREMENT*/
	private Integer id;
	
	private Integer infoerId;
	
	private Integer orderId;
	
	private Date date;
	
	private BigDecimal amount;
	
	private String remark;
	
	
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getInfoerId() {
		return infoerId;
	}

	public void setInfoerId(Integer infoerId) {
		this.infoerId = infoerId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}