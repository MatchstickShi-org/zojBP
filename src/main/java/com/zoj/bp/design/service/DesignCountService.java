package com.zoj.bp.design.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.DesignCount;
import com.zoj.bp.design.dao.IDesignCountDao;

@Service
public class DesignCountService implements IDesignCountService
{
	@Autowired
	private IDesignCountDao dao;

	@Override
	public List<DesignCount> getTodayDesignerCount()
	{
		return dao.getTodayDesignerCount();
	}
}
