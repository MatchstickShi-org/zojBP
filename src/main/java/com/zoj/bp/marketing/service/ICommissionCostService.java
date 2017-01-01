package com.zoj.bp.marketing.service;

import com.zoj.bp.common.model.CommissionCost;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

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
