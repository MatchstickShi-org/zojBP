/**  */
package com.decoration.bp.common.model;

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
	
	private Integer todayInfoerVisitCount;  //当日信息员回访数
	
	private Integer todayClientVisitCount;  //当日客户回访数（客户跟踪）
	
	private Integer todayOrderVisitCount;  	//当日在谈单回访数
	
	private Integer todayInfoerAddCount;  	//当日信息员录入数
	
	private Integer tracingInfoerCount;		//正跟踪信息员总数
		
	private Integer todayClientAddCount;	//当日客户录入数
	
	private Integer contactingClientCount;	//联系中的客户总数
	
	private Integer talkingOrderCount;		//当前在谈单数量
	
	private Integer dealOrderCount;			//已签单数
	
	private Integer monthTalkingOrderCount;	//本月提单数量（已申请在谈单）
	
	private Integer applyTalkingOrderCount;	//当日提单数量（已申请在谈单）
	
	private Integer salesmanId;				//业务员Id
	
	private String salesmanName;			//业务员姓名
	
	private Integer leaderId;				//上级主管Id

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getTodayInfoerVisitCount() {
		return todayInfoerVisitCount;
	}

	public void setTodayInfoerVisitCount(Integer todayInfoerVisitCount) {
		this.todayInfoerVisitCount = todayInfoerVisitCount;
	}

	public Integer getTodayOrderVisitCount() {
		return todayOrderVisitCount;
	}

	public void setTodayOrderVisitCount(Integer todayOrderVisitCount) {
		this.todayOrderVisitCount = todayOrderVisitCount;
	}

	public Integer getTodayClientVisitCount() {
		return todayClientVisitCount;
	}

	public void setTodayClientVisitCount(Integer todayClientVisitCount) {
		this.todayClientVisitCount = todayClientVisitCount;
	}

	public Integer getTodayInfoerAddCount() {
		return todayInfoerAddCount;
	}

	public void setTodayInfoerAddCount(Integer todayInfoerAddCount) {
		this.todayInfoerAddCount = todayInfoerAddCount;
	}

	public Integer getTodayClientAddCount() {
		return todayClientAddCount;
	}

	public void setTodayClientAddCount(Integer todayClientAddCount) {
		this.todayClientAddCount = todayClientAddCount;
	}

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public Integer getTracingInfoerCount() {
		return tracingInfoerCount;
	}

	public void setTracingInfoerCount(Integer tracingInfoerCount) {
		this.tracingInfoerCount = tracingInfoerCount;
	}

	public Integer getContactingClientCount() {
		return contactingClientCount;
	}

	public void setContactingClientCount(Integer contactingClientCount) {
		this.contactingClientCount = contactingClientCount;
	}

	public Integer getTalkingOrderCount() {
		return talkingOrderCount;
	}

	public void setTalkingOrderCount(Integer talkingOrderCount) {
		this.talkingOrderCount = talkingOrderCount;
	}

	public Integer getDealOrderCount() {
		return dealOrderCount;
	}

	public void setDealOrderCount(Integer dealOrderCount) {
		this.dealOrderCount = dealOrderCount;
	}

	public Integer getMonthTalkingOrderCount() {
		return monthTalkingOrderCount;
	}

	public void setMonthTalkingOrderCount(Integer monthTalkingOrderCount) {
		this.monthTalkingOrderCount = monthTalkingOrderCount;
	}

	public Integer getApplyTalkingOrderCount() {
		return applyTalkingOrderCount;
	}

	public void setApplyTalkingOrderCount(Integer applyTalkingOrderCount) {
		this.applyTalkingOrderCount = applyTalkingOrderCount;
	}

	public Integer getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(Integer leaderId) {
		this.leaderId = leaderId;
	}
}