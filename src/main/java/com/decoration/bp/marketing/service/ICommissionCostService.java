package com.decoration.bp.marketing.service;

import com.decoration.bp.common.model.CommissionCost;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

public interface ICommissionCostService {

	/**
	 * @param pagination
	 * @param infoerId 
	 * @param orderId 
	 * @return
	 */
	DatagridVo<CommissionCost> getAllCommissionCost(Pagination pagination,Integer infoerId,Integer orderId);

	/**
	 * @param commissionCost
	 * @return 
	 */
	Integer addCommissionCost(CommissionCost commissionCost);
}
