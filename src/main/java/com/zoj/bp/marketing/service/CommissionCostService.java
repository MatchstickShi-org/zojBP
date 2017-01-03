package com.zoj.bp.marketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.CommissionCost;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.ICommissionCostDao;

@Service
public class CommissionCostService implements ICommissionCostService{

	@Autowired
	private ICommissionCostDao dao;

	@Override
	public DatagridVo<CommissionCost> getAllCommissionCost(Pagination pagination, Integer infoerId,Integer orderId)
	{
		return dao.getAllCommissionCost(pagination, infoerId,orderId);
	}

	@Override
	public Integer addCommissionCost(CommissionCost commissionCost) {
		return dao.addCommissionCost(commissionCost);
	}

}
