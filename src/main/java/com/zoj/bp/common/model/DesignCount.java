/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;

import org.springframework.format.annotation.NumberFormat;

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
	
	private Integer todayOrderVisitCount;  //当日在谈单回访数
	
	private Integer talkingOrderCount;	//当前在谈单数量
	
	private Integer dealOrderCount;		//已签单数
	
	private Integer deadOrderCount;		//死单总数
	
	@NumberFormat(pattern="#,###.##")
	@Digits(fraction = 2, integer = Integer.MAX_VALUE)
	private BigDecimal monthDealAmount;	//本月签单总额
	
	@NumberFormat(pattern="#,###.##")
	@Digits(fraction = 2, integer = Integer.MAX_VALUE)
	private BigDecimal totalDealAmount;	//累计签单总额
	
	private Integer designerId;			//设计师Id
	
	private String designerName;		//设计师姓名
	
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

	public Integer getTodayOrderVisitCount() {
		return todayOrderVisitCount;
	}

	public void setTodayOrderVisitCount(Integer todayOrderVisitCount) {
		this.todayOrderVisitCount = todayOrderVisitCount;
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

	public Integer getDeadOrderCount() {
		return deadOrderCount;
	}

	public void setDeadOrderCount(Integer deadOrderCount) {
		this.deadOrderCount = deadOrderCount;
	}

	public Integer getDesignerId() {
		return designerId;
	}

	public void setDesignerId(Integer designerId) {
		this.designerId = designerId;
	}

	public String getDesignerName() {
		return designerName;
	}

	public void setDesignerName(String designerName) {
		this.designerName = designerName;
	}

	public BigDecimal getMonthDealAmount() {
		return monthDealAmount;
	}

	public void setMonthDealAmount(BigDecimal monthDealAmount) {
		this.monthDealAmount = monthDealAmount;
	}

	public BigDecimal getTotalDealAmount() {
		return totalDealAmount;
	}

	public void setTotalDealAmount(BigDecimal totalDealAmount) {
		this.totalDealAmount = totalDealAmount;
	}
}