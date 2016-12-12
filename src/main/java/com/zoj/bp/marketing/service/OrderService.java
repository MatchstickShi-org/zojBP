package com.zoj.bp.marketing.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.IOrderDao;

public class OrderService implements IOrderService {
	
	@Autowired
	private IOrderDao dao;

	@Override
	public Order getOrderById(Integer id) {
		return dao.getOrderById(id);
	}

	@Override
	public void updateOrder(Order order) {
		dao.updateOrder(order);
	}

	@Override
	public DatagridVo<Order> getAllOrder(Pagination pagination, User loginUser) {
		return dao.getAllOrder(pagination, loginUser);
	}

	@Override
	public Integer addOrder(Order order) {
		return dao.addOrder(order);
	}

}
