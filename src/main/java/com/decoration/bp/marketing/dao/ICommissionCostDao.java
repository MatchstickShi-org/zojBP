/**
 * 
 */
package com.decoration.bp.marketing.dao;

import com.decoration.bp.common.model.CommissionCost;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

/**
 * @author wangw
 *
 */
public interface ICommissionCostDao
{
	/**
	 * @param commissionCost
	 * @return 
	 */
	Integer addCommissionCost(CommissionCost commissionCost);
	/**
	 * @param pagination
	 * @param infoerId
	 * @param orderId
	 * @return
	 */
	DatagridVo<CommissionCost> getAllCommissionCost(Pagination pagination,Integer infoerId,Integer orderId);

	
}