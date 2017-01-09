package com.zoj.bp.design.service;

import java.util.List;

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
	public DatagridVo<DesignerVisitApply> getAllDesignerVisitApply(Pagination pagination,String designerName,Integer orderId,Integer... status) {
		return dao.getAllDesignerVisitApply(pagination, designerName, orderId,status);
	}
	
	@Override
	public Integer updateVisitApply(DesignerVisitApply designerVisitApply) {
		return dao.updateVisitApply(designerVisitApply);
	}
	
	@Override
	public Integer addDesignerVisitApply(DesignerVisitApply designerVisitApply) {
		return dao.addDesignerVisitApply(designerVisitApply);
	}
	
	@Override
	public List<DesignerVisitApply> getDesignerVisitApplyByOrderId(Integer orderId) {
		return dao.getDesignerVisitApplyByOrderId(orderId);
	}

	@Override
	public DesignerVisitApply getDesignerVisitApplyById(Integer id) {
		return dao.getDesignerVisitApplyById(id);
	}
}
