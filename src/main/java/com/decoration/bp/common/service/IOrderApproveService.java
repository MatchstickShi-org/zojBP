/**
 * 
 */
package com.decoration.bp.common.service;

import com.decoration.bp.common.model.OrderApprove;
import com.decoration.bp.common.model.User;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

/**
 * @author wangw
 *
 */
public interface IOrderApproveService
{
	/**
	 * @param orderApprove
	 * @return 
	 */
	Integer addOrderApprove(OrderApprove orderApprove);
	/**
	 * @param pagination
	 * @param loginUser
	 * @param orderId
	 * @return
	 */
	DatagridVo<OrderApprove> getAllOrderApprove(Pagination pagination,User loginUser,Integer orderId);
}