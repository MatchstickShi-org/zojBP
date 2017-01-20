/**
 * 
 */
package com.zoj.bp.marketing.dao;

import java.util.List;

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
	 * 获取指定日期的商务部统计记录
	 * @param pagination
	 * @param salesmanName 业务员名称
	 * @param startDate 开始日期
	 * @param endDate 截至日期
	 * @return
	 */
	DatagridVo<MarketingCount> getTodayMarketingCout(Pagination pagination,String salesmanName,String startDate,String endDate);

	/**
	 * 获取前一天的统计数据（定时任务用）
	 * @param userId 用户Id
	 * @param startDate 开始日期
	 * @param endDate 截至日期
	 * @return
	 */
	MarketingCount getTodayMarketingCountByUserId(Integer userId,String startDate,String endDate);

	/**
	 * 新增业务员统计
	 * @param marketingCount
	 * @return
	 */
	Integer addMarketingCount(MarketingCount marketingCount);

	/**
	 * 根据用户Id获取最近一次统计记录
	 * @param userId
	 * @return
	 */
	List<MarketingCount> getLastMarketingCountByUsetrId(Integer userId);
}