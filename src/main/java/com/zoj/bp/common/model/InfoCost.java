/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wangw
 */
public class InfoCost implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3426935117405508997L;

	/**id INT(20) not null AUTO_INCREMENT*/
	private Integer id;
	
	private Integer infoerId;
	
	private Integer orderId;
	
	private Integer orderStatus;
	
	private String date;
	
	private BigDecimal amount;
	
	private String remark;
	
	private String projectName;
	
	private String infoerName;
	
	private String salesmanName;
	
	private String designerName;
	
	
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date.substring(0,19);
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getInfoerName() {
		return infoerName;
	}

	public void setInfoerName(String infoerName) {
		this.infoerName = infoerName;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public String getDesignerName() {
		return designerName;
	}

	public void setDesignerName(String designerName) {
		this.designerName = designerName;
	}

	public Integer getOrderStatus()
	{
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus)
	{
		this.orderStatus = orderStatus;
	}
}