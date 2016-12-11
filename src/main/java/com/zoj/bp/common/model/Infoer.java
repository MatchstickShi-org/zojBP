/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
	
	@NotNull
	private String org;
	
	@NotNull
	private String address;
	
	@NotNull
	private Integer tel;
	
	private Integer tel2;
	
	private Integer tel3;
	
	private Integer tel4;
	
	private Integer tel5;
	
	@NotNull
	private Integer level;
	
	@NotNull
	private Integer salesmanId;
	
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

	public Integer getTel() {
		return tel;
	}

	public void setTel(Integer tel) {
		this.tel = tel;
	}

	public Integer getTel2() {
		return tel2;
	}

	public void setTel2(Integer tel2) {
		this.tel2 = tel2;
	}

	public Integer getTel3() {
		return tel3;
	}

	public void setTel3(Integer tel3) {
		this.tel3 = tel3;
	}

	public Integer getTel4() {
		return tel4;
	}

	public void setTel4(Integer tel4) {
		this.tel4 = tel4;
	}

	public Integer getTel5() {
		return tel5;
	}

	public void setTel5(Integer tel5) {
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
}