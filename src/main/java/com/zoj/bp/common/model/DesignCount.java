/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangw
 */
public class DesignCount implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8872584269064727058L;

	/**id INT(20) not null AUTO_INCREMENT*/
	private Integer id;
	
	private Date updateTime;
	
	private Date countDate;
	
	private Integer talkingVistiAmount;  //当日在谈单回访数
	
	private Integer talkingAmount;		//当前在谈单数量
	
	private Integer dealTotal;			//已签单数
	
	private Integer deadTotal;			//死单总数
	
	private Integer designerId;			//设计师Id
	
	private Integer designerName;		//设计师姓名
	
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}

	public Integer getTalkingVistiAmount() {
		return talkingVistiAmount;
	}

	public void setTalkingVistiAmount(Integer talkingVistiAmount) {
		this.talkingVistiAmount = talkingVistiAmount;
	}

	public Integer getTalkingAmount() {
		return talkingAmount;
	}

	public void setTalkingAmount(Integer talkingAmount) {
		this.talkingAmount = talkingAmount;
	}

	public Integer getDealTotal() {
		return dealTotal;
	}

	public void setDealTotal(Integer dealTotal) {
		this.dealTotal = dealTotal;
	}

	public Integer getDeadTotal() {
		return deadTotal;
	}

	public void setDeadTotal(Integer deadTotal) {
		this.deadTotal = deadTotal;
	}

	public Integer getDesignerId() {
		return designerId;
	}

	public void setDesignerId(Integer designerId) {
		this.designerId = designerId;
	}

	public Integer getDesignerName() {
		return designerName;
	}

	public void setDesignerName(Integer designerName) {
		this.designerName = designerName;
	}
}