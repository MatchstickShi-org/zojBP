/**
 * 
 */
package com.zoj.bp.marketing.dao;

import com.zoj.bp.common.model.MarketingCount;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author wangw
 *
 */
public interface IMarketingCountDao
{
	/**
	 * 获取当天商务部的统计记录
	 * @param pagination
	 * @param salesmanName
	 * @return
	 */
	DatagridVo<MarketingCount> getTodayMarketingCout(Pagination pagination,String salesmanName);

	/**
	 * 获取前一天的统计数据（定时任务用）
	 * @param userId
	 * @return
	 */
	MarketingCount getTodayMarketingCountByUserId(Integer userId);

	/**
	 * 新增业务员统计
	 * @param marketingCount
	 * @return
	 */
	Integer addMarketingCount(MarketingCount marketingCount);
}