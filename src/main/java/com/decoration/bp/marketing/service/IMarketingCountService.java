package com.decoration.bp.marketing.service;

import java.util.List;

import com.decoration.bp.common.model.MarketingCount;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

public interface IMarketingCountService {

	/**
	 * 获取当天的商务部统计记录
	 * @param pagination
	 * @param salesmanName 信息员名称
	 * @return
	 */
	DatagridVo<MarketingCount> getTodayMarketingCount(Pagination pagination,String salesmanName);
	
	/**
	 * 获取指定日期的商务部统计记录
	 * @param pagination
	 * @param salesmanName 信息员名称
	 * @param startDate 开始日期
	 * @param endDate 截至日期
	 * @return
	 */
	DatagridVo<MarketingCount> getMarketingCountByDate(Pagination pagination,String salesmanName,String startDate,String endDate);

	/**
	 * 获取前一天的统计数据（定时任务用）
	 * @param userId 用户Id
	 * @param countDate 统计日期（数据日期）
	 * @return
	 */
	MarketingCount getTodayMarketingCountByUserId(Integer userId,String countDate);
	
	/**
	 * 新增业务员统计
	 * @param marketingCount
	 * @return
	 */
	Integer addMarketingCount(MarketingCount marketingCount);
	
	/**
	 * 根据用户Id获取最近一次统计记录
	 * @param userId 用户Id
	 * @return
	 */
	List<MarketingCount> getLastMarketingCountByUsetrId(Integer userId);
}
