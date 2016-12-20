/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * @author wangw
 */
public class InfoerVisit implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -860295797898088776L;

	/**id INT(20) not null AUTO_INCREMENT*/
	private Integer id;
	
	private Integer infoerId;
	
	private Integer salesmanId;
	
	private String date;
	
	@NotNull
	private String content;
	
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getInfoerId() {
		return infoerId;
	}

	public void setInfoerId(Integer infoerId) {
		this.infoerId = infoerId;
	}

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}