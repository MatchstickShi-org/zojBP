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
	
	/**
	 * 获取当天的在谈单回访记录
	 * @param pagination
	 * @param userId 用户Id
	 * @return
	 */
	DatagridVo<OrderVisit> getTodayTalkingOrderVisitByUserId(Pagination pagination,Integer userId);
	
	/**
	 * @param id
	 * @return
	 */
	OrderVisit getOrderVisitById(Integer id);

	/**
	 * @param orderVisit
	 */
	void updateOrderVisit(OrderVisit orderVisit);
	
	/**
	 * @param salesmanIds
	 */
	Integer deleteBySalesmanId(Integer... salesmanIds);
	
	/**
	 * @param designerIds
	 * @return
	 */
	Integer deleteByDesignerId(Integer... designerIds);
}