/**
 * 
 */
package com.zoj.bp.marketing.dao;

import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author wangw
 *
 */
public interface IOrderVisitDao
{
	/**
	 * @param orderVisit
	 * @return 
	 */
	Integer addOrderVisit(OrderVisit orderVisit);
	/**
	 * @param pagination
	 * @param visitorId
	 * @param orderId
	 * @return
	 */
	DatagridVo<OrderVisit> getAllOrderVisit(Pagination pagination,Integer visitorId,Integer orderId);

	
}