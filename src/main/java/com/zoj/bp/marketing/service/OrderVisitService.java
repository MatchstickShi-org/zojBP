package com.zoj.bp.marketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.IOrderVisitDao;

@Service
public class OrderVisitService implements IOrderVisitService{

	@Autowired
	private IOrderVisitDao dao;

	@Override
	public DatagridVo<OrderVisit> getAllOrderVisit(Pagination pagination,Integer visitorId,Integer orderId) {
		return dao.getAllOrderVisit(pagination,visitorId,orderId);
	}

	@Override
	public Integer addOrderVisit(OrderVisit orderVisit) {
		return dao.addOrderVisit(orderVisit);
	}

}
