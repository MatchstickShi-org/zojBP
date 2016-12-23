package com.zoj.bp.marketing.service;

import com.zoj.bp.common.model.Client;
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
	 * @param client
	 * @return
	 */
	void updateOrder(Order order,Client client);

	/**
	 * @param pagination
	 * @param loginUser
	 * @param infoerId
	 * @param status
	 * @return
	 */
	DatagridVo<Order> getAllOrder(Pagination pagination,User loginUser,Integer infoerId,Integer[] status);

	/**
	 * 
	 * @param pagination
	 * @param loginUser
	 * @param name
	 * @param tel
	 * @param infoerName
	 * @param status
	 * @return
	 */
	DatagridVo<Order> getAllOrder(Pagination pagination,User loginUser, String name, String tel, String infoerName, String[] status);
	/**
	 * @param order
	 * @return 
	 */
	Integer addOrder(Order order);

	void addOrderAndClient(Order order);

	Order findByTel(String tel);
}
