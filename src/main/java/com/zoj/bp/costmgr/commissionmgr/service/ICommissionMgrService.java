/**  */
package com.zoj.bp.costmgr.commissionmgr.service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.costmgr.commissionmgr.vo.CommissionCost;

/**
 * @author MatchstickShi
 */
public interface ICommissionMgrService
{
	/**
	 * @param user
	 * @param clientName
	 * @param orderId
	 * @param pagination
	 * @return
	 * @throws BusinessException
	 */
	DatagridVo<CommissionCost> getAllCommissionCosts(User user, String clientName, String orderId, Pagination pagination)
			throws BusinessException;
}