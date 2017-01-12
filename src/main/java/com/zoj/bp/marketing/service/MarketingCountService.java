package com.zoj.bp.marketing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.MarketingCount;
import com.zoj.bp.marketing.dao.IMarketingCountDao;

@Service
public class MarketingCountService implements IMarketingCountService{

	@Autowired
	private IMarketingCountDao dao;

	@Override
	public List<MarketingCount> getTodayMarketingCount() {
		return dao.getTodayMarketingCout();
	}
}
