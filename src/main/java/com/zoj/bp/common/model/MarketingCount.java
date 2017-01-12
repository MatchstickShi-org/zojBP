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
	private static final long serialVersionUID = -6404533723061984832L;

	/**id INT(20) not null AUTO_INCREMENT*/
	private Integer id;
	
	private Date updateTime;
	
	private Date countDate;
	
	private Integer infoerVisitAmount;   //当日信息员回访数
	
	private Integer talkingVistiAmount;  //当日在谈单回访数
	
	private Integer infoerAddAmount;  	//当日信息员录入数
	
	private Integer infoerTracingTotal;	//正跟踪信息员总数
		
	private Integer clientAddAmount;	//当日客户录入数
	
	private Integer contactingClinetTotal;//联系中的客户总数
	
	private Integer talkingAmount;		//当前在谈单数量
	
	private Integer dealTotal;			//已签单数
	
	private Integer monthTalkingAmount;	//本月提单数量（已申请在谈单）
	
	private Integer salesmanId;			//业务员Id
	
	private Integer salesmanName;			//业务员姓名
	
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

	public Integer getInfoerVisitAmount() {
		return infoerVisitAmount;
	}

	public void setInfoerVisitAmount(Integer infoerVisitAmount) {
		this.infoerVisitAmount = infoerVisitAmount;
	}

	public Integer getInfoerAddAmount() {
		return infoerAddAmount;
	}

	public void setInfoerAddAmount(Integer infoerAddAmount) {
		this.infoerAddAmount = infoerAddAmount;
	}

	public Integer getInfoerTracingTotal() {
		return infoerTracingTotal;
	}

	public void setInfoerTracingTotal(Integer infoerTracingTotal) {
		this.infoerTracingTotal = infoerTracingTotal;
	}

	public Integer getContactingClinetTotal() {
		return contactingClinetTotal;
	}

	public void setContactingClinetTotal(Integer contactingClinetTotal) {
		this.contactingClinetTotal = contactingClinetTotal;
	}

	public Integer getMonthTalkingAmount() {
		return monthTalkingAmount;
	}

	public void setMonthTalkingAmount(Integer monthTalkingAmount) {
		this.monthTalkingAmount = monthTalkingAmount;
	}

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public Integer getTalkingVistiAmount() {
		return talkingVistiAmount;
	}

	public void setTalkingVistiAmount(Integer talkingVistiAmount) {
		this.talkingVistiAmount = talkingVistiAmount;
	}

	public Integer getClientAddAmount() {
		return clientAddAmount;
	}

	public void setClientAddAmount(Integer clientAddAmount) {
		this.clientAddAmount = clientAddAmount;
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

	public Integer getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(Integer salesmanName) {
		this.salesmanName = salesmanName;
	}
}