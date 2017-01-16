package com.zoj.bp.costmgr.commissionmgr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.costmgr.commissionmgr.dao.ICommissionCostMgrDao;
import com.zoj.bp.costmgr.commissionmgr.vo.CommissionCost;

/**
 * @author MatchstickShi
 */
@Service
public class CommissionCostMgrService implements ICommissionCostMgrService
{
	@Autowired
	private ICommissionCostMgrDao commissionCostDao;
	
	@Override
	public DatagridVo<CommissionCost> getCommissionCostsByOrder(Integer orderId, Pagination pagination)
	{
		return commissionCostDao.getCommissionCostsByOrder(orderId, pagination);
	}

	@Override
	public Integer addCommissionCostRecord(CommissionCost commissionCost) {
		return commissionCostDao.addCommissionCostRecord(commissionCost);
	}

	@Override
	public CommissionCost getCommissionCostByOrder(Integer orderId) {
		return commissionCostDao.getCommissionCostByOrder(orderId);
	}
}