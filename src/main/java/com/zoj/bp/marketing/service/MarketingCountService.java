package com.zoj.bp.marketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.MarketingCount;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.IMarketingCountDao;

@Service
public class MarketingCountService implements IMarketingCountService{

	@Autowired
	private IMarketingCountDao dao;

	@Override
	public DatagridVo<MarketingCount> getTodayMarketingCount(Pagination pagination,String salesmanName,String startDate,String endDate) {
		return dao.getTodayMarketingCout(pagination,salesmanName,startDate,endDate);
	}

	@Override
	public MarketingCount getTodayMarketingCountByUserId(Integer userId) {
		return dao.getTodayMarketingCountByUserId(userId);
	}

	@Override
	public Integer addMarketingCount(MarketingCount marketingCount) {
		return dao.addMarketingCount(marketingCount);
	}
}
