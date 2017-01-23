package com.zoj.bp.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.dao.IOrderChangeLogDao;
import com.zoj.bp.common.model.OrderChangeLog;
@Service
public class OrderChangeLogService implements IOrderChangeLogService {

	@Autowired
	private IOrderChangeLogDao dao;
	
	@Override
	public List<OrderChangeLog> getOrderChangeLogByOrderId(Integer orderId) {
		return dao.getOrderChangeLogByOrderId(orderId);
	}
	
	@Override
	public Integer addOrderChangeLog(OrderChangeLog orderChangeLog) {
		return dao.addOrderChangeLog(orderChangeLog);
	}
}
