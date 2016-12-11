/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangw
 */
public class MarketingCount implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8872584269064727058L;

	/**id INT(20) not null AUTO_INCREMENT*/
	private Integer id;
	
	private Date updateTime;
	
	private Date countDate;
	
	private Integer talkingVistiAmount;
	
	private Integer talkingAmount;
	
	private Integer dealTotal;
	
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
}