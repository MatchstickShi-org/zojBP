package com.zoj.bp.design.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.design.dao.IDesignerVisitApplyDao;
import com.zoj.bp.design.vo.DesignerVisitApply;
@Service
public class DesignerVisitApplyService implements IDesignerVisitApplyService {

	@Autowired
	private IDesignerVisitApplyDao dao;
	
	@Override
	public DatagridVo<DesignerVisitApply> getAllDesignerVisitApply(Pagination pagination,String designerName,Integer orderId) {
		return dao.getAllDesignerVisitApply(pagination, designerName, orderId);
	}
	
	@Override
	public Integer addDesignerVisitApply(DesignerVisitApply designerVisitApply) {
		return dao.addDesignerVisitApply(designerVisitApply);
	}
}
