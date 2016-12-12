/**
 * 
 */
package com.zoj.bp.marketing.dao;

import com.zoj.bp.common.model.InfoCost;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author wangw
 *
 */
public interface IInfoCostDao
{
	/**
	 * @param infoCost
	 * @return 
	 */
	Integer addInfoCost(InfoCost infoCost);
	/**
	 * @param pagination
	 * @param infoCost
	 * @return
	 */
	DatagridVo<InfoCost> getAllInfoCost(Pagination pagination,Integer infoCost);

	
}