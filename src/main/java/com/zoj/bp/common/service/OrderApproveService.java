package com.zoj.bp.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.dao.IOrderApproveDao;
import com.zoj.bp.common.model.OrderApprove;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
@Service
public class OrderApproveService implements IOrderApproveService {

	@Autowired
	private IOrderApproveDao dao;
	
	@Override
	public DatagridVo<OrderApprove> getAllOrderApprove(Pagination pagination,User loginUser,Integer orderId) {
		return dao.getAllOrderApprove(pagination, loginUser, orderId);
	}
	
	@Override
	public Integer addOrderApprove(OrderApprove orderApprove) {
		return dao.addOrderApprove(orderApprove);
	}
}
