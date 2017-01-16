package com.zoj.bp.design.service;

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
	public DatagridVo<DesignCount> getTodayDesignerCount(Pagination pagination,String designerName)
	{
		return dao.getTodayDesignerCount(pagination,designerName);
	}

	@Override
	public DesignCount getTodayDesignCountByUserId(Integer userId) {
		return dao.getTodayDesignCountByUserId(userId);
	}

	@Override
	public Integer addDesignCount(DesignCount designCount) {
		return dao.addDesignCount(designCount);
	}
}
