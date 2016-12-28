package com.zoj.bp.marketing.service;

import com.zoj.bp.common.model.Client;
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.OrderApprove;
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

	/**
	 * 放弃客户
	 * @param orderIds
	 */
	Integer deleteOrderByIds(Integer[] orderIds);

	/**
	 * 申请再谈单
	 * @param orderApprove
	 * @return
	 */
	Integer addOrderApprove(OrderApprove orderApprove);
	
	/**
	 * 更新订单的业务员Id
	 * @param orderIds
	 * @param salesmanId
	 * @return
	 */
	Integer updateOrderSalesmanId(Integer[] orderIds, Integer salesmanId);
	
	/**
	 * 更新订单的设计师Id
	 * @param orderIds
	 * @param designerId
	 * @return
	 */
	Integer updateOrderDesigerId(Integer[] orderIds, Integer designerId);

	/**
	 * 根据信息Id更新订单的业务员Id
	 * @param infoerIds
	 * @param salesmanId
	 * @return
	 */
	Integer updateOrderSalesmanIdByInfoers(Integer[] infoerIds, Integer salesmanId);
}
