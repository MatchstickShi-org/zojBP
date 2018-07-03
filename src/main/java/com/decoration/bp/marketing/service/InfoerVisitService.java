package com.decoration.bp.marketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.decoration.bp.common.model.InfoerVisit;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;
import com.decoration.bp.marketing.dao.IInfoerVisitDao;

@Service
public class InfoerVisitService implements IInfoerVisitService{

	@Autowired
	private IInfoerVisitDao infoerVisitDao;

	@Override
	public DatagridVo<InfoerVisit> getAllInfoerVisit(Pagination pagination, Integer infoerId) {
		return infoerVisitDao.getAllInfoerVisit(pagination, infoerId);
	}

	@Override
	public Integer addInfoerVisit(InfoerVisit infoerVisit) {
		return infoerVisitDao.addInfoerVisit(infoerVisit);
	}

}
