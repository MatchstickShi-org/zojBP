/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;

/**
 * @author wangw
 */
public class Client implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8570690225369612255L;

	/**id INT(20) not null AUTO_INCREMENT*/
	private Integer id;
	
	private Integer orderId;
	
	private String name;
	
	private String orgAddr;
	
	private String tel;
	
	private String tel2;
	
	private String tel3;
	
	private String tel4;
	
	private String tel5;
	
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgAddr() {
		return orgAddr;
	}

	public void setOrgAddr(String orgAddr) {
		this.orgAddr = orgAddr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
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

}