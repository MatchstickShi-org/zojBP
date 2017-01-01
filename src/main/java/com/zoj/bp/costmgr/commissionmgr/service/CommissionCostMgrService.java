package com.zoj.bp.costmgr.commissionmgr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.costmgr.commissionmgr.dao.ICommissionCostMgrDao;
import com.zoj.bp.costmgr.commissionmgr.vo.CommissionCost;

/**
 * @author MatchstickShi
 */
@Service
public class CommissionCostMgrService implements ICommissionMgrService
{
	@Autowired
	private ICommissionCostMgrDao commissionCostDao;
	
	@Override
	public DatagridVo<CommissionCost> getAllCommissionCosts(
			User user, String clientName, String orderId, Pagination pagination) throws BusinessException
	{
		if(user.isDesignLeader() || user.isDesignDesigner() || user.isDesignManager() || user.isAdmin())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是市场部人员，无法查询。"));
		return commissionCostDao.getAllCommissionCosts(user, clientName, orderId, pagination);
	}
}