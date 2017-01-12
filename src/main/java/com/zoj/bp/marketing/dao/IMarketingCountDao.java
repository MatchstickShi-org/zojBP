/**
 * 
 */
package com.zoj.bp.marketing.dao;

import java.util.List;

import com.zoj.bp.common.model.MarketingCount;

/**
 * @author wangw
 *
 */
public interface IMarketingCountDao
{
	/**
	 * 获取当天商务部的统计记录
	 * @return
	 */
	List<MarketingCount> getTodayMarketingCout();
}