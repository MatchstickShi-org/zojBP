package com.zoj.bp.marketing.service;

import com.zoj.bp.common.model.MarketingCount;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

public interface IMarketingCountService {

	/**
	 * 获取当天的商务部统计记录
	 * @param pagination
	 * @param salesmanName 信息员名称
	 * @return
	 */
	DatagridVo<MarketingCount> getTodayMarketingCount(Pagination pagination,String salesmanName);

	/**
	 * 获取前一天的统计数据（定时任务用）
	 * @param userId 用户Id
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
