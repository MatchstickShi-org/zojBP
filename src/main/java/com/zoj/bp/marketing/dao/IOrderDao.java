package com.zoj.bp.marketing.dao;

import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.Order.Status;
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
	 * @param infoerId	信息员Id
	 * @param status	状态
	 * @return
	 */
	DatagridVo<Order> getOrdersByInfoer(Pagination pagination,Integer infoerId,Integer[] status);

	/**
	 * 
	 * @param pagination
	 * @param user TODO
	 * @param clientName
	 * @param tel
	 * @param infoerName
	 * @param isKey TODO
	 * @param statuses
	 * @return
	 */
	DatagridVo<Order> getOrdersByUser(Pagination pagination,
			User user, String clientName, String tel, String infoerName, Integer isKey, Integer... statuses);
	/**
	 * @param order
	 * @return 
	 */
	Integer addOrder(Order order);

	/**
	 * 更新订单状态
	 * @param order
	 */
	Integer updateOrderStatus(Order order);

	/**
	 * 设置客户状态为已放弃
	 * @param status TODO
	 * @param orderIds
	 */
	Integer updateOrderStatus(Status status, Integer... orderIds);
	
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

	/**
	 * @param pagination
	 * @param salesman
	 * @param name
	 * @param tel
	 * @param infoerName
	 * @param isKey TODO
	 * @param statuses
	 * @return
	 */
	DatagridVo<Order> getOrdersBySalesman(Pagination pagination, User salesman,
			String name, String tel, String infoerName, Integer isKey, Integer... statuses);

	/**
	 * @param userIds
	 */
	Integer deleteBySalesmans(Integer... salesmanIds);

	/**
	 * @param designerIds
	 * @return
	 */
	Integer deleteByDesigners(Integer... designerIds);

	/**
	 * @param pagination
	 * @param designer
	 * @param name
	 * @param tel
	 * @param infoerName
	 * @param isKey TODO
	 * @param statuses
	 * @return
	 */
	DatagridVo<Order> getOrdersByDesigner(Pagination pagination, User designer, String name, String tel, String infoerName,
			Integer isKey, Integer... statuses);

	DatagridVo<Order> getOrdersByStatus(User loginUser, String clientName, Integer orderId, Pagination pagination, Status... status) throws Exception;
}