package com.zoj.bp.marketing.dao;

import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

public interface IOrderDao {
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
	 * @return
	 */
	DatagridVo<Order> getAllOrder(Pagination pagination,User loginUser);

	/**
	 * @param order
	 * @return 
	 */
	Integer addOrder(Order order);
}
