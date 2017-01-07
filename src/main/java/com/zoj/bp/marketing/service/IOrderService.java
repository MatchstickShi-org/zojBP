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
	 * @param loginUser TODO
	 * @return
	 */
	Order getOrderById(Integer id, User loginUser);

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
	DatagridVo<Order> getOrdersByInfoer(Pagination pagination,User loginUser,Integer infoerId,Integer... status);

	/**
	 * 
	 * @param loginUser TODO
	 * @param pagination 分页对象
	 * @param designerId 设计师Id
	 * @param name 客户名称
	 * @param tel	客户电话
	 * @param infoerName 信息员名称
	 * @param designerName	设计师名称
	 * @param status 状态
	 * @return
	 */
	DatagridVo<Order> getOrdersByUser(User loginUser,Pagination pagination,Integer designerId, String name, String tel, String infoerName,String designerName,Integer... status);
	/**
	 * @param order
	 * @return 
	 */
	Integer addOrder(Order order);

	void addOrderAndClient(Order order);

	Order findByTels(User loginUser, String... tels);

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

	/**
	 * @param salesman
	 * @param pagination
	 * @param name
	 * @param tel
	 * @param infoerName
	 * @param empty
	 * @param status
	 * @return
	 */
	DatagridVo<Order> getOrdersBySalesman(User salesman, Pagination pagination, 
			String name, String tel, String infoerName, String empty, Integer... status);
}
