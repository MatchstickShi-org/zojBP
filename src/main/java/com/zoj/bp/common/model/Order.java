package com.zoj.bp.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.NumberFormat;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author wangw
 */
public class Order implements Serializable
{
	private static final long serialVersionUID = -1941948674701203482L;

	private Integer id;

	private Integer infoerId;

	private String infoerName;

	private Integer salesmanId;

	private String salesmanName;

	private Integer salesmanStatus;

	private Integer designerId;

	private String designerName;

	private Integer designerStatus;

	private String projectName;

	private String projectAddr;

	private String insertTime;
	
	private String putOrderTime; //提单时间

	private Integer status;

	@NotNull
	private String name;

	private String orgAddr;

	private String tel1;

	private String tel2;

	private String tel3;

	private String tel4;

	private String tel5;
	
	/**
	 * 是否重点客户（1：是；0：否）
	 */
	private Integer isKey = 0;
	/**
	 * 是否待联系客户（1：是；0：否）
	 */
	private Integer isWait = 0;
	
	@NumberFormat(pattern="#,###.##")
	@Digits(fraction = 2, integer = Integer.MAX_VALUE)
	private BigDecimal dealAmount;

	/** 未回访天数 */
	private Integer notVisitDays;
	
	/** 设计师回访申请状态 */
	private Integer visitApplyStatus;

	public boolean canLookTel(User user)
	{
		if(!user.isLeader())
			return true;
		else if(user.isLeader())
		{
			if(user.isBelongMarketing())
				return user.getId() == this.getSalesmanId();
			else if(user.isBelongDesign())
				return user.getId() == this.getDesignerId();
		}
		return false;
	}

	public void hideAllTel(User user)
	{
		if (!this.canLookTel(user))
		{
			Order that = this;
			Optional.ofNullable(tel1).ifPresent(tel -> that.setTel1("******"));
			Optional.ofNullable(tel2).ifPresent(tel -> that.setTel2(null));
			Optional.ofNullable(tel3).ifPresent(tel -> that.setTel3(null));
			Optional.ofNullable(tel4).ifPresent(tel -> that.setTel4(null));
			Optional.ofNullable(tel5).ifPresent(tel -> that.setTel5(null));
		}
	}

	public enum Status
	{
		/** 正跟踪:10 */
		tracing(10),
		/** 已放弃:12 */
		abandoned(12),
		/** 在谈单-设计师已打回:14 */
		designerRejected(14),
		/** 打回中-主案部经理审核:20 */
		rejectingDesignManagerAuditing(20),
		/** 打回中-商务部经理审核:22 */
		rejectingMarketingManagerAuditing(22),
		/** 在谈单-商务部经理审核中:30 */
		talkingMarketingManagerAuditing(30),
		/** 在谈单-主案部经理审核中:32 */
		talkingDesignManagerAuditing(32),
		/** 在谈单-设计师跟踪中:34 */
		talkingDesignerTracing(34),
		/** 已签单:90 */
		deal(90),
		/** 死单:0 */
		dead(0),
		/** 不准单-主案部经理审核中:60 */
		disagreeDesignManagerAuditing(60),
		/** 不准单-商务部经理审核中:62 */
		disagreeMarketingManagerAuditing(62),
		/** 不准单:64 */
		disagree(64);
		private Status(Integer value)
		{
			this.value = value;
		}

		private Integer value;

		public Integer value()
		{
			return this.value;
		}

		public static Status valueOf(Integer status)
		{
			return Stream.of(Status.values()).filter(r -> r.value() == status).findFirst().orElse(null);
		}
		
		@Override
		public String toString()
		{
			return String.valueOf(this.value);
		}
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getInfoerId()
	{
		return infoerId;
	}

	public void setInfoerId(Integer infoerId)
	{
		this.infoerId = infoerId;
	}

	public Integer getSalesmanId()
	{
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId)
	{
		this.salesmanId = salesmanId;
	}

	public Integer getDesignerId()
	{
		return designerId;
	}

	public void setDesignerId(Integer designerId)
	{
		this.designerId = designerId;
	}

	public String getDesignerName()
	{
		return designerName;
	}

	public void setDesignerName(String designerName)
	{
		this.designerName = designerName;
	}

	public String getProjectName()
	{
		return projectName;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	public String getProjectAddr()
	{
		return projectAddr;
	}

	public void setProjectAddr(String projectAddr)
	{
		this.projectAddr = projectAddr;
	}

	public String getInsertTime()
	{
		return insertTime;
	}

	public void setInsertTime(String insertTime)
	{
		this.insertTime = insertTime.substring(0, 19);
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getInfoerName()
	{
		return infoerName;
	}

	public void setInfoerName(String infoerName)
	{
		this.infoerName = infoerName;
	}

	public String getSalesmanName()
	{
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName)
	{
		this.salesmanName = salesmanName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getOrgAddr()
	{
		return orgAddr;
	}

	public void setOrgAddr(String orgAddr)
	{
		this.orgAddr = orgAddr;
	}

	public String getTel1()
	{
		return tel1;
	}

	public void setTel1(String tel1)
	{
		this.tel1 = tel1;
	}

	public String getTel2()
	{
		return tel2;
	}

	public void setTel2(String tel2)
	{
		this.tel2 = tel2;
	}

	public String getTel3()
	{
		return tel3;
	}

	public void setTel3(String tel3)
	{
		this.tel3 = tel3;
	}

	public String getTel4()
	{
		return tel4;
	}

	public void setTel4(String tel4)
	{
		this.tel4 = tel4;
	}

	public String getTel5()
	{
		return tel5;
	}

	public void setTel5(String tel5)
	{
		this.tel5 = tel5;
	}

	public Integer getNotVisitDays()
	{
		return notVisitDays;
	}

	public void setNotVisitDays(Integer notVisitDays)
	{
		this.notVisitDays = notVisitDays;
	}

	public String getTelAll()
	{
		return StringUtils.join(getTels(), ", ");
	}

	/**
	 * @return
	 */
	@JSONField(serialize=false)
	public List<String> getTels()
	{
		List<String> tels = new ArrayList<>();
		if (StringUtils.isNotEmpty(tel1)) tels.add(tel1);
		if (StringUtils.isNotEmpty(tel2)) tels.add(tel2);
		if (StringUtils.isNotEmpty(tel3)) tels.add(tel3);
		if (StringUtils.isNotEmpty(tel4)) tels.add(tel4);
		if (StringUtils.isNotEmpty(tel5)) tels.add(tel5);
		return tels;
	}

	public Integer getSalesmanStatus()
	{
		return salesmanStatus;
	}

	public void setSalesmanStatus(Integer salesmanStatus)
	{
		this.salesmanStatus = salesmanStatus;
	}

	public Integer getDesignerStatus()
	{
		return designerStatus;
	}

	public void setDesignerStatus(Integer designerStatus)
	{
		this.designerStatus = designerStatus;
	}

	public Integer getVisitApplyStatus()
	{
		return visitApplyStatus;
	}

	public void setVisitApplyStatus(Integer visitApplyStatus)
	{
		this.visitApplyStatus = visitApplyStatus;
	}

	public Integer getIsKey()
	{
		return isKey;
	}

	public void setIsKey(Integer isKey)
	{
		this.isKey = isKey;
	}

	public Integer getIsWait() {
		return isWait;
	}

	public void setIsWait(Integer isWait) {
		this.isWait = isWait;
	}

	public BigDecimal getDealAmount() {
		return dealAmount;
	}

	public void setDealAmount(BigDecimal dealAmount) {
		this.dealAmount = dealAmount;
	}

	public String getPutOrderTime() {
		return putOrderTime;
	}

	public void setPutOrderTime(String putOrderTime) {
		this.putOrderTime = StringUtils.isEmpty(putOrderTime) ? putOrderTime:putOrderTime.substring(0, 19) ;
	}
	
}