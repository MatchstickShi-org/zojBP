package com.zoj.bp.costmgr.infocostmgr.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author MatchstickShi
 */
public class InfoCost implements Serializable
{
	private static final long serialVersionUID = 3239296569294785547L;
	
	private Integer id;
	
	@NotNull
	private Integer orderId;
	
	private Integer orderStatus;
	
	@NotNull
	private Integer clientId;
	
	private String clientName;
	
	private String projectAddr;
	
	@NotNull
	private Integer infoerId;
	
	private String infoer;
	
	private Integer designerId;
	
	private String designer;
	
	@NotNull
	private Integer salesmanId;
	
	private String salesman;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JSONField(format="yyyy-MM-dd")
	private Date remitDate;
	
	@NumberFormat(pattern="#,###.##")
	@NotNull
	@Digits(fraction = 2, integer = Integer.MAX_VALUE)
	private BigDecimal cost;
	
	private String remark;

	public Integer getOrderId()
	{
		return orderId;
	}

	public void setOrderId(Integer orderId)
	{
		this.orderId = orderId;
	}

	public Integer getClientId()
	{
		return clientId;
	}

	public void setClientId(Integer clientId)
	{
		this.clientId = clientId;
	}

	public String getClientName()
	{
		return clientName;
	}

	public void setClientName(String clientName)
	{
		this.clientName = clientName;
	}

	public String getProjectAddr()
	{
		return projectAddr;
	}

	public void setProjectAddr(String projectAddr)
	{
		this.projectAddr = projectAddr;
	}

	public Integer getInfoerId()
	{
		return infoerId;
	}

	public void setInfoerId(Integer infoerId)
	{
		this.infoerId = infoerId;
	}

	public String getInfoer()
	{
		return infoer;
	}

	public void setInfoer(String infoer)
	{
		this.infoer = infoer;
	}

	public Integer getDesignerId()
	{
		return designerId;
	}

	public void setDesignerId(Integer designerId)
	{
		this.designerId = designerId;
	}

	public String getDesigner()
	{
		return designer;
	}

	public void setDesigner(String designer)
	{
		this.designer = designer;
	}

	public Integer getSalesmanId()
	{
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId)
	{
		this.salesmanId = salesmanId;
	}

	public String getSalesman()
	{
		return salesman;
	}

	public void setSalesman(String salesman)
	{
		this.salesman = salesman;
	}

	public Date getRemitDate()
	{
		return remitDate;
	}

	public void setRemitDate(Date remitDate)
	{
		this.remitDate = remitDate;
	}

	public BigDecimal getCost()
	{
		return cost;
	}

	public void setCost(BigDecimal cost)
	{
		this.cost = cost;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
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