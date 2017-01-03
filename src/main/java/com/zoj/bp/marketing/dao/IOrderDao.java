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
	 * 
	 * @param pagination
	 * @param salesmanId
	 * @param designerId
	 * @param name
	 * @param tel
	 * @param infoerName
	 * @param designerName
	 * @param status
	 * @return
	 */
	DatagridVo<Order> getAllOrder(Pagination pagination,Integer salesmanId,Integer designerId,String name, String tel, String infoerName,String designerName,String[] status);
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
	 * @param orderIds
	 */
	Integer updateOrderByIds(Integer[] orderIds);
	
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
