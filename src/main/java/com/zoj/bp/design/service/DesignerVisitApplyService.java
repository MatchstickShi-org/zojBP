package com.zoj.bp.design.service;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.design.dao.IDesignerVisitApplyDao;
import com.zoj.bp.design.vo.DesignerVisitApply;

@Service
public class DesignerVisitApplyService implements IDesignerVisitApplyService
{
	@Autowired
	private IDesignerVisitApplyDao dao;

	@Override
	public DatagridVo<DesignerVisitApply> getTodayDesignerVisitApplys(Pagination pagination,
			String designerName, Integer orderId, Integer... status)
	{
		return dao.getTodayDesignerVisitApplys(pagination, designerName, orderId, status);
	}

	@Override
	public Integer updateVisitApply(DesignerVisitApply designerVisitApply)
	{
		return dao.updateVisitApply(designerVisitApply);
	}

	@Override
	public Integer addDesignerVisitApply(DesignerVisitApply designerVisitApply) throws BusinessException
	{
		if (CollectionUtils.isNotEmpty(dao.getTodayApplysByOrder(designerVisitApply.getOrderId())))
			throw new BusinessException(ReturnCode.ILLEGALITY_OPERATION.setMsg("对不起，你今天已经提交过回访申请，无法再次申请，请耐心等待。"));
		return dao.addDesignerVisitApply(designerVisitApply);
	}

	@Override
	public DesignerVisitApply getDesignerVisitApplyById(Integer id)
	{
		return dao.getDesignerVisitApplyById(id);
	}
}
