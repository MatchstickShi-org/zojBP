/**
 * 
 */
package com.decoration.bp.common.dao;

import java.util.List;

import com.decoration.bp.common.model.OrderChangeLog;

/**
 * @author wangw
 *
 */
public interface IOrderChangeLogDao
{
	/**
	 * @param orderChangeLog
	 * @return 
	 */
	Integer addOrderChangeLog(OrderChangeLog orderChangeLog);
	/**
	 * @param orderId
	 * @return
	 */
	List<OrderChangeLog> getOrderChangeLogByOrderId(Integer orderId);
	
	/**
	 * @param salesmanIds
	 */
	Integer deleteBySalesmans(Integer... salesmanIds);
	/**
	 * @param designerIds
	 * @return
	 */
	Integer deleteByDesigners(Integer... designerIds);
}