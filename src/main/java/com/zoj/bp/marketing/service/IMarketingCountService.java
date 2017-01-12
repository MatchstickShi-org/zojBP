package com.zoj.bp.marketing.service;

import java.util.List;

import com.zoj.bp.common.model.MarketingCount;

public interface IMarketingCountService {

	/**
	 * 获取当天的商务部统计记录
	 * @return
	 */
	List<MarketingCount> getTodayMarketingCount();
}
