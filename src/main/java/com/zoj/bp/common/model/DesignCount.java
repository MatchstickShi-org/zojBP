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
	private static final long serialVersionUID = -6404533723061984832L;

	/**id INT(20) not null AUTO_INCREMENT*/
	private Integer id;
	
	private Date updateTime;
	
	private Date countDate;
	
	private Integer srcVisitAmount;
	
	private Integer talkingVistiAmount;
	
	private Integer srcAddAmount;
	
	private Integer srcAddTotal;
	
	private Integer clientAddAmount;
	
	private Integer clinetAddTotal;
	
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

	public Integer getSrcVisitAmount() {
		return srcVisitAmount;
	}

	public void setSrcVisitAmount(Integer srcVisitAmount) {
		this.srcVisitAmount = srcVisitAmount;
	}

	public Integer getTalkingVistiAmount() {
		return talkingVistiAmount;
	}

	public void setTalkingVistiAmount(Integer talkingVistiAmount) {
		this.talkingVistiAmount = talkingVistiAmount;
	}

	public Integer getSrcAddAmount() {
		return srcAddAmount;
	}

	public void setSrcAddAmount(Integer srcAddAmount) {
		this.srcAddAmount = srcAddAmount;
	}

	public Integer getSrcAddTotal() {
		return srcAddTotal;
	}

	public void setSrcAddTotal(Integer srcAddTotal) {
		this.srcAddTotal = srcAddTotal;
	}

	public Integer getClientAddAmount() {
		return clientAddAmount;
	}

	public void setClientAddAmount(Integer clientAddAmount) {
		this.clientAddAmount = clientAddAmount;
	}

	public Integer getClinetAddTotal() {
		return clinetAddTotal;
	}

	public void setClinetAddTotal(Integer clinetAddTotal) {
		this.clinetAddTotal = clinetAddTotal;
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