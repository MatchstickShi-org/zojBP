/**
 * 
 */
package com.zoj.bp.common.vo;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author Administrator
 *
 */
public class Pagination implements Serializable
{
	private static final long serialVersionUID = 8818943089282068700L;
	
	private Integer page = 1;
	
	private Integer rows = 10;
	
	private String orderBy;
	
	private String ascOrDesc = "ASC";
	
	public Pagination()
	{
		super();
	}

	private Integer startRow = 0;

	/**
	 * @param page
	 * @param rows
	 */
	public Pagination(Integer page, Integer rows)
	{
		super();
		this.page = NumberUtils.max(page, 1);
		this.rows = NumberUtils.max(rows, 1);
		this.startRow = this.page * this.rows - this.rows;
	}

	public Integer getPage()
	{
		return page;
	}

	public void setPage(Integer page)
	{
		this.page = NumberUtils.max(page, 1);
		this.startRow = this.page * this.rows - this.rows;
	}

	public Integer getRows()
	{
		return rows;
	}

	public void setRows(Integer rows)
	{
		this.rows = NumberUtils.max(rows, 1);
		this.startRow = this.page * this.rows - this.rows;
	}

	public Integer getStartRow()
	{
		return startRow;
	}

	public String getOrderBy()
	{
		return orderBy;
	}

	public void setSort(String orderBy)
	{
		this.orderBy = orderBy;
	}

	public String getAscOrDesc()
	{
		return ascOrDesc;
	}

	public void setOrder(String ascOrDesc)
	{
		this.ascOrDesc = ascOrDesc;
	}

	/**
	 * 构建order by 子句，不包括 “ORDER BY”关键字
	 * @param defaultOrderByIfNull
	 * @return
	 */
	public String buildOrderBySqlPart(String defaultOrderByIfNull)
	{
		if(StringUtils.isEmpty(this.orderBy))
			return StringUtils.defaultString(defaultOrderByIfNull);
		return " " + this.getOrderBy() + " " + this.getAscOrDesc() + " ";
	}
}