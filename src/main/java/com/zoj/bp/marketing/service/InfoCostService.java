package com.zoj.bp.marketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.InfoCost;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.IInfoCostDao;

@Service
public class InfoCostService implements IInfoCostService{

	@Autowired
	private IInfoCostDao infoCostDao;

	@Override
	public DatagridVo<InfoCost> getAllInfoCost(Pagination pagination, Integer infoerId,Integer orderId)
	{
		return infoCostDao.getAllInfoCost(pagination,infoerId,orderId);
	}

	@Override
	public Integer addInfoCost(InfoCost infoCost) {
		return infoCostDao.addInfoCost(infoCost);
	}

}
