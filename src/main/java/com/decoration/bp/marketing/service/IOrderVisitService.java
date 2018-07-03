package com.decoration.bp.marketing.service;

import com.decoration.bp.common.model.OrderVisit;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

public interface IOrderVisitService {

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
	 * @param orderVisit
	 * @return 
	 */
	Integer addOrderVisit(OrderVisit orderVisit);

	/**
	 * 
	 * @param id
	 * @return
	 */
	OrderVisit getOrderVisitById(Integer id);

	/**
	 * @param orderVisit
	 */
	void updateOrderVisit(OrderVisit orderVisit);
}
