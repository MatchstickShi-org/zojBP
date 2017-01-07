package com.zoj.bp.costmgr.commissionmgr.dao;

import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.costmgr.commissionmgr.vo.CommissionCost;

/**
 * @author MatchstickShi
 */
public interface ICommissionCostMgrDao
{

	/**
	 * @param user
	 * @param orderId 
	 * @param clientName 
	 * @param pagination
	 */
	DatagridVo<CommissionCost> getAllCommissionCosts(User user, String clientName, String orderId, Pagination pagination);

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