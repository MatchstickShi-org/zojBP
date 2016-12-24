/**
 * 
 */
package com.zoj.bp.marketing.dao;

import com.zoj.bp.common.model.OrderApprove;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author wangw
 *
 */
public interface IOrderApproveDao
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