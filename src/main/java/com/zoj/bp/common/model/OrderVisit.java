/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;

/**
 * @author wangw
 */
public class OrderVisit implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3582205535685323884L;

	/**id INT(20) not null AUTO_INCREMENT*/
	private Integer id;
	
	private Integer orderId;
	
	private Integer visitorId;
	
	private String date;
	
	private String content;
	
	
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

	public Integer getVisitorId() {
		return visitorId;
	}

	public void setVisitorId(Integer visitorId) {
		this.visitorId = visitorId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date.substring(0, 19);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}