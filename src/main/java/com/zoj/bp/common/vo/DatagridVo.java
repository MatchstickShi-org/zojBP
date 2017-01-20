/**
 * 
 */
package com.zoj.bp.common.vo;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.zoj.bp.common.excption.ReturnCode;

/**
 * @author Administrator
 *
 */
public class DatagridVo<T> implements Serializable
{
	private static final long serialVersionUID = 4420500801282833901L;
	
	private int total;
	
	private boolean flag;
	
	private int returnCode;
	
	private String msg;

	private List<T> rows;
	
	private Map<String, Object> extParams = new HashMap<>();
	
	private DatagridVo(List<T> beanList, int totalRows, ReturnCode returnCode)
	{
		this.rows = beanList;
		this.total = totalRows;
		this.returnCode = returnCode.getValue();
		this.msg = returnCode.getMsg();
	}
	
	private DatagridVo(List<T> beanList, int totalRows)
	{
		this.rows = beanList;
		this.total = totalRows;
	}
	
	public static <Y> DatagridVo<Y> emptyVo(ReturnCode returnCode)
	{
		return new DatagridVo<>(Collections.<Y>emptyList(), 0, returnCode);
	}
	
	public static <Y> DatagridVo<Y> buildDatagridVo(List<Y> beanList, Integer totalRows)
	{
		if(totalRows == null)
			totalRows = beanList.size();
		return new DatagridVo<>(beanList, totalRows);
	}
	
	public static <Y> DatagridVo<Y> buildDatagridVo(List<Y> beanList)
	{
		return buildDatagridVo(beanList, null);
	}

	public List<T> getRows()
	{
		return rows;
	}

	public int getTotal()
	{
		return total;
	}

	public boolean isFlag()
	{
		return flag;
	}

	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}

	public int getReturnCode()
	{
		return returnCode;
	}

	public void setReturnCode(ReturnCode returnCode)
	{
		this.returnCode = returnCode.getValue();
	}

	public String getMsg()
	{
		return msg;
	}

	public DatagridVo<T> setMsg(String msg)
	{
		this.msg = msg;
		return this;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public void setRows(List<T> rows)
	{
		this.rows = rows;
	}
	
	public DatagridVo<T> removeRows(DatagridVo<T> removeVo)
	{
		if(CollectionUtils.isNotEmpty(this.getRows()) && removeVo != null && CollectionUtils.isNotEmpty(removeVo.getRows()))
			this.getRows().removeAll(removeVo.getRows());
		return this;
	}
	
	public DatagridVo<T> removeRows(List<T> removeRows)
	{
		if(CollectionUtils.isNotEmpty(this.getRows()) && CollectionUtils.isNotEmpty(removeRows))
			this.getRows().removeAll(removeRows);
		return this;
	}
	
	public void addExtParam(String key, Object value)
	{
		this.extParams.put(key, value);
	}

	public Map<String, Object> getExtParams()
	{
		return extParams;
	}

	public void setExtParams(Map<String, Object> extParams)
	{
		this.extParams = extParams;
	}
}