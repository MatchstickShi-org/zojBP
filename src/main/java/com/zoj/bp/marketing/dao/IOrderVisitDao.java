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
	/**
	 * 更新回访记录的回访者Id
	 * @param infoerIds 信息员Id
	 * @param salesmanId 业务员Id
	 * @return
	 */
	Integer updateVisitorIdByInfoers(Integer[] infoerIds, Integer salesmanId);
	/**
	 * 更新回访记录的回访者Id
	 * @param orderIds 客户Id
	 * @param userId 转移接收者Id
	 * @return
	 */
	Integer updateVisitorIdByOrderIds(Integer[] orderIds, Integer userId);
}