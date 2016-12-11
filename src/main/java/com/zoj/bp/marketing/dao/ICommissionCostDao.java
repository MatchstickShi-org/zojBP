/**
 * 
 */
package com.zoj.bp.marketing.dao;

import com.zoj.bp.common.model.CommissionCost;
import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

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
	 * @param infoer
	 * @return
	 */
	DatagridVo<CommissionCost> getAllCommissionCost(Pagination pagination,Infoer infoer);

	
}