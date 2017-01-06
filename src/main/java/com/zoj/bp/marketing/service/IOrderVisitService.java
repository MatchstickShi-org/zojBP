package com.zoj.bp.marketing.service;

import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

public interface IOrderVisitService {

	/**
	 * @param pagination
	 * @param orderId 
	 * @return
	 */
	DatagridVo<OrderVisit> getAllOrderVisit(Pagination pagination,Integer visitorId,Integer orderId);

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
