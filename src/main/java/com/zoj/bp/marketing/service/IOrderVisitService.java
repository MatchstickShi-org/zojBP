package com.zoj.bp.marketing.service;

import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

public interface IOrderVisitService {

	/**
	 * @param pagination
	 * @param orderId 
	 * @return
	 */
	DatagridVo<OrderVisit> getAllOrderVisit(Pagination pagination,User loginUser,Integer orderId);

	/**
	 * @param orderVisit
	 * @return 
	 */
	Integer addOrderVisit(OrderVisit orderVisit);
}
