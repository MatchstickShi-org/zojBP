package com.zoj.bp.marketing.service;

import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

public interface IOrderService {
	/**
	 * @param id
	 * @return
	 */
	Order getOrderById(Integer id);

	/**
	 * @param order
	 * @return
	 */
	void updateOrder(Order order);

	/**
	 * @param pagination
	 * @param status 
	 * @param infoerName 
	 * @param tel 
	 * @param name 
	 * @return
	 */
	DatagridVo<Order> getAllOrder(Pagination pagination,User loginUser, String name, String tel, String infoerName, String status);

	/**
	 * @param order
	 * @return 
	 */
	Integer addOrder(Order order);
}
