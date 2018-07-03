/**  */
package com.decoration.bp.costmgr.commissionmgr.service;

import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;
import com.decoration.bp.costmgr.commissionmgr.vo.CommissionCost;

/**
 * @author MatchstickShi
 */
public interface ICommissionCostMgrService
{
	/**
	 * @param orderId
	 * @param pagination
	 * @return
	 */
	DatagridVo<CommissionCost> getCommissionCostsByOrder(Integer orderId, Pagination pagination);
	
	/**
	 * @param commissionCost
	 * @return
	 */
	Integer addCommissionCostRecord(CommissionCost commissionCost);
	
	/**
	 * @param orderId
	 * @return
	 */
	CommissionCost getCommissionCostByOrder(Integer orderId);
}