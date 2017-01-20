package com.zoj.bp.marketing.service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.Order.Status;
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
	 * @param loginUser TODO
	 * @return
	 */
	void updateOrder(Order order, User loginUser);

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
	 * @param name 客户名称
	 * @param orderId 单号
	 * @param tel	客户电话
	 * @param infoerName 信息员名称
	 * @param salesmanOrDesignerId TODO
	 * @param isKey TODO
	 * @param status 状态
	 * @return
	 */
	DatagridVo<Order> getOrdersByUser(User loginUser,Pagination pagination,String name,Integer orderId, String tel, String infoerName, Integer salesmanOrDesignerId, Integer isKey, Integer... status);
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
	Integer giveUpOrders(Integer... orderIds);

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
	 * @param clientName
	 * @param orderId
	 * @param tel
	 * @param infoerName
	 * @param salesmanId TODO
	 * @param isKey TODO
	 * @param status
	 * @return
	 */
	DatagridVo<Order> getOrdersBySalesman(User salesman, Pagination pagination, 
			String clientName,Integer orderId,String tel, String infoerName, Integer salesmanId, Integer isKey, Integer... status);

	/**
	 * @param pagination
	 * @param designer
	 * @param clientName
	 * @param orderId
	 * @param tel
	 * @param infoerName
	 * @param designerId TODO
	 * @param status
	 * @return
	 */
	DatagridVo<Order> getOrdersByDesigner(Pagination pagination, User designer, String clientName,Integer orderId, String tel, String infoerName,
			Integer designerId, Integer... status);

	DatagridVo<Order> getOrdersByStatus(User loginUser, String clientName, Integer orderId, Pagination pagination, Status... status) throws Exception;

	Integer setOrder2Tracing(Integer orderId) throws BusinessException;
}
