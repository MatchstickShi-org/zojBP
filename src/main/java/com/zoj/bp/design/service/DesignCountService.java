package com.zoj.bp.design.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.DesignCount;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.design.dao.IDesignCountDao;

@Service
public class DesignCountService implements IDesignCountService
{
	@Autowired
	private IDesignCountDao dao;

	@Override
	public DatagridVo<DesignCount> getTodayDesignerCount(Pagination pagination,String designerName,String startDate,String endDate)
	{
		return dao.getTodayDesignerCount(pagination,designerName,startDate,endDate);
	}

	@Override
	public DesignCount getTodayDesignCountByUserId(Integer userId,String startDate,String endDate) {
		return dao.getTodayDesignCountByUserId(userId,startDate,endDate);
	}

	@Override
	public Integer addDesignCount(DesignCount designCount) {
		return dao.addDesignCount(designCount);
	}

	@Override
	public List<DesignCount> getLastDesignCountByUsetrId(Integer userId) {
		return dao.getLastDesignCountByUsetrId(userId);
	}
}
