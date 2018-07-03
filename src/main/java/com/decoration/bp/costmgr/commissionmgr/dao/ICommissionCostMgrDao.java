package com.decoration.bp.costmgr.commissionmgr.dao;

import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;
import com.decoration.bp.costmgr.commissionmgr.vo.CommissionCost;

/**
 * @author MatchstickShi
 */
public interface ICommissionCostMgrDao
{

	/**
	 * @param orderId
	 * @param pagination
	 */
	DatagridVo<CommissionCost> getCommissionCostsByOrder(Integer orderId, Pagination pagination);

	/**
	 * @param infoCost
	 * @return
	 */
	Integer addCommissionCostRecord(CommissionCost commissionCost);

	/**
	 * @param orderId
	 * @return
	 */
	CommissionCost getCommissionCostByOrder(Integer orderId);

}