/**
 * 
 */
package com.zoj.bp.common.vo;

import java.io.Serializable;

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
}