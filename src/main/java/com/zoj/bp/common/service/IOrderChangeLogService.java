/**
 * 
 */
package com.zoj.bp.common.service;

import java.util.List;

import com.zoj.bp.common.model.OrderChangeLog;

/**
 * @author wangw
 *
 */
public interface IOrderChangeLogService
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
}