/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

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
	private String nature;
	
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
	
	private String insertTime;
	
	private Integer leftVisitDays;  //剩余回访天数
	
	@SuppressWarnings("unused")
	private String telAll;
	
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

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
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

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel1() {
		return tel1;
	}

	public void setTel1(String tel1) {
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

	public void setLevel(Integer level) {
		this.level = level;
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

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime.substring(0,19);
	}

	public String getTelAll() {
		String telAll = StringUtils.EMPTY;
		if(StringUtils.isNotEmpty(tel1))
			telAll +=tel1;
		if(StringUtils.isNotEmpty(tel2))
			telAll = telAll+" "+tel2;
		if(StringUtils.isNotEmpty(tel3))
			telAll = telAll+" "+tel3;
		if(StringUtils.isNotEmpty(tel4))
			telAll = telAll+" "+tel4;
		if(StringUtils.isNotEmpty(tel5))
			telAll = telAll+" "+tel5;
		return telAll;
	}

	public void setTelAll(String telAll) {
		this.telAll = telAll;
	}
	
}