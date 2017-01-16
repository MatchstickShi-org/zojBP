/**  */
package com.zoj.bp.costmgr.commissionmgr.service;

import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.costmgr.commissionmgr.vo.CommissionCost;

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