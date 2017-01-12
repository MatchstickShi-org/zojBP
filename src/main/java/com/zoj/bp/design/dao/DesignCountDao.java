package com.zoj.bp.design.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.DesignCount;

@Repository
public class DesignCountDao extends BaseDao implements IDesignCountDao{

	@Override
	public List<DesignCount> getTodayDesignerCount() {
		// TODO Auto-generated method stub
		return null;
	}
}
