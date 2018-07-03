package com.decoration.bp.marketing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.decoration.bp.common.model.MarketingCount;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;
import com.decoration.bp.marketing.dao.IMarketingCountDao;

@Service
public class MarketingCountService implements IMarketingCountService{

	@Autowired
	private IMarketingCountDao dao;

	@Override
	public DatagridVo<MarketingCount> getTodayMarketingCount(Pagination pagination,String salesmanName) {
		return dao.getTodayMarketingCount(pagination,salesmanName);
	}
	
	@Override
	public DatagridVo<MarketingCount> getMarketingCountByDate(Pagination pagination,String salesmanName,String startDate,String endDate) {
		return dao.getMarketingCountByDate(pagination,salesmanName,startDate,endDate);
	}

	@Override
	public MarketingCount getTodayMarketingCountByUserId(Integer userId,String countDate) {
		return dao.getTodayMarketingCountByUserId(userId,countDate);
	}

	@Override
	public Integer addMarketingCount(MarketingCount marketingCount) {
		return dao.addMarketingCount(marketingCount);
	}

	@Override
	public List<MarketingCount> getLastMarketingCountByUsetrId(Integer userId) {
		return dao.getLastMarketingCountByUsetrId(userId);
	}
}
