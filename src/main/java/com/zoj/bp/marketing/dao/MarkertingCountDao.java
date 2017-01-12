package com.zoj.bp.marketing.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.MarketingCount;

@Repository
public class MarkertingCountDao extends BaseDao implements IMarketingCountDao
{

	@Override
	public List<MarketingCount> getTodayMarketingCout() {
		return null;
	}
	
}
