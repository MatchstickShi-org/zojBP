package com.zoj.bp.marketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.IOrderDao;
import com.zoj.bp.marketing.dao.IOrderVisitDao;

@Service
public class OrderVisitService implements IOrderVisitService{

	@Autowired
	private IOrderVisitDao dao;
	
	@Autowired
	private IOrderDao orderDao;

	@Override
	public DatagridVo<OrderVisit> getAllOrderVisit(Pagination pagination, Integer visitorId, Integer orderId)
	{
		return dao.getAllOrderVisit(pagination, visitorId, orderId);
	}

	@Override
	public Integer addOrderVisit(OrderVisit orderVisit) {
		Order order = orderDao.getOrderById(orderVisit.getOrderId());
		orderVisit.setOrderStatus(order.getStatus());
		return dao.addOrderVisit(orderVisit);
	}

	@Override
	public OrderVisit getOrderVisitById(Integer id) {
		return dao.getOrderVisitById(id);
	}

	@Override
	public void updateOrderVisit(OrderVisit orderVisit) {
		dao.updateOrderVisit(orderVisit);
	}

	@Override
	public DatagridVo<OrderVisit> getTodayTalkingOrderVisitByUserId(Pagination pagination, Integer userId) {
		return dao.getTodayTalkingOrderVisitByUserId(pagination, userId);
	}

}
