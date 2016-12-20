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
	 * @param loginUser 业务员
	 * @param infoerId	信息员Id
	 * @param status	状态
	 * @return
	 */
	DatagridVo<Order> getAllOrder(Pagination pagination,User loginUser,Integer infoerId,Integer[] status);

	/**
	 * @param pagination
	 * @param status 
	 * @param infoerName 
	 * @param tel 
	 * @param name 
	 * @return
	 */
	DatagridVo<Order> getAllOrder(Pagination pagination,User loginUser, String name, String tel, String infoerName, Integer[] status);
	/**
	 * @param order
	 * @return 
	 */
	Integer addOrder(Order order);
}
