/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.zoj.bp.common.model.Order.Status;

/**
 * @author wangw
 */
public class Infoer implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4569248844186467595L;
	/**id INT(20) not null AUTO_INCREMENT*/
	private Integer id;
	/**name*/
	@NotNull
	private String name;
	
	@NotNull
	private Integer nature;
	
	private String org;
	
	private String address;
	
	private String tel1;
	
	private String tel2;
	
	private String tel3;
	
	private String tel4;
	
	private String tel5;
	
	private Integer level;
	
	private String levelDesc; //等级对应的中文
	
	private Integer salesmanId;
	
	private String salesmanName;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date insertTime;
	
	private Integer leftVisitDays;  //未回访天数
	
	/** 最后回访时间 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date lastVisitDate;
	
	@SuppressWarnings("unused")
	private String telAll;
	
	private Integer isWait = 0;
	
	public enum Level
	{
		/**金牌*/
		gold(1),
		/**银牌*/
		silver(2),
		/**铜牌*/
		bronze(3),
		/**铁牌*/
		iron(4);
		
		private Level(Integer value)
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
	}
	
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getNature() {
		return nature;
	}

	public void setNature(Integer nature) {
		this.nature = nature;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public boolean canLookTel(User user)
	{
		return !user.isLeader() || (user.isLeader() && user.getId() == this.getSalesmanId());
	}
	
	public void hideAllTel(User user)
	{
		if(!this.canLookTel(user))
		{
			Infoer that = this;
			Optional.ofNullable(tel1).ifPresent(tel -> that.setTel1("******"));
			Optional.ofNullable(tel2).ifPresent(tel -> that.setTel2(null));
			Optional.ofNullable(tel3).ifPresent(tel -> that.setTel3(null));
			Optional.ofNullable(tel4).ifPresent(tel -> that.setTel4(null));
			Optional.ofNullable(tel5).ifPresent(tel -> that.setTel5(null));
		}
	}

	public String getTel1()
	{
		return tel1;
	}

	public void setTel1(String tel1)
	{
		this.tel1 = tel1;
	}
	
	public String getLevelDesc() {
		return levelDesc;
	}

	public void setLevelDesc(String levelDesc) {
		this.levelDesc = levelDesc;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getTel3() {
		return tel3;
	}

	public void setTel3(String tel3) {
		this.tel3 = tel3;
	}

	public String getTel4() {
		return tel4;
	}

	public void setTel4(String tel4) {
		this.tel4 = tel4;
	}

	public String getTel5() {
		return tel5;
	}

	public void setTel5(String tel5) {
		this.tel5 = tel5;
	}

	public Integer getLevel() {
		return level;
	}

	public boolean setLevel(int level)
	{
		if(this.level == null || this.level < level)
			return false;
		this.level = level;
		return true;
	}
	
	public void setLevel(Integer level)
	{
		this.level = level;
	}
	
	public boolean setLevel(Level level)
	{
		if(this.level == null || this.level < level.value || level == null)
			return false;
		this.level = level.value;
		return true;
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

	public Integer getLeftVisitDays() {
		return leftVisitDays;
	}

	public void setLeftVisitDays(Integer leftVisitDays) {
		this.leftVisitDays = leftVisitDays;
	}

	public Date getInsertTime()
	{
		return insertTime;
	}

	public void setInsertTime(Date insertTime)
	{
		this.insertTime = insertTime;
	}

	public String getTelAll() {
		String telAll = StringUtils.EMPTY;
		if(StringUtils.isNotEmpty(tel1))
			telAll +=tel1;
		if(StringUtils.isNotEmpty(tel2))
			telAll = telAll+", "+tel2;
		if(StringUtils.isNotEmpty(tel3))
			telAll = telAll+", "+tel3;
		if(StringUtils.isNotEmpty(tel4))
			telAll = telAll+", "+tel4;
		if(StringUtils.isNotEmpty(tel5))
			telAll = telAll+", "+tel5;
		return telAll;
	}

	public void setTelAll(String telAll) {
		this.telAll = telAll;
	}

	public Date getLastVisitDate()
	{
		return lastVisitDate;
	}

	public void setLastVisitDate(Date lastVisitDate)
	{
		this.lastVisitDate = lastVisitDate;
	}

	public Integer getIsWait() {
		return isWait;
	}

	public void setIsWait(Integer isWait) {
		this.isWait = isWait;
	}
}