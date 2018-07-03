package com.decoration.bp.marketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.decoration.bp.common.model.CommissionCost;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;
import com.decoration.bp.marketing.dao.ICommissionCostDao;

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
