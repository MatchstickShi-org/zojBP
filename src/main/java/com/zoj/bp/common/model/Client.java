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
	
	private String orgAddr;
	
	private Integer tel;
	
	private Integer tel2;
	
	private Integer tel3;
	
	private Integer tel4;
	
	private Integer tel5;
	
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

	public String getOrgAddr() {
		return orgAddr;
	}

	public void setOrgAddr(String orgAddr) {
		this.orgAddr = orgAddr;
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

}