/**
 * 
 */
package com.decoration.bp.marketing.dao;

import com.decoration.bp.common.model.InfoCost;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

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
	 * @param infoerId
	 * @param orderId
	 * @return
	 */
	DatagridVo<InfoCost> getAllInfoCost(Pagination pagination,Integer infoerId,Integer orderId);

	
}