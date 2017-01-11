/**
 * 
 */
package com.zoj.bp.design.service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.design.vo.DesignerVisitApply;

/**
 * @author wangw
 *
 */
public interface IDesignerVisitApplyService
{
	/**
	 * @param designerVisitApply
	 * @return 
	 * @throws BusinessException 
	 */
	Integer addDesignerVisitApply(DesignerVisitApply designerVisitApply) throws BusinessException;
	/**
	 * @param pagination
	 * @param designerName
	 * @param orderId
	 * @param status
	 * @return
	 */
	DatagridVo<DesignerVisitApply> getTodayDesignerVisitApplys(Pagination pagination,String designerName,Integer orderId,Integer... status);
	
	/**
	 * 更新回访申请
	 * @param designerVisitApply
	 * @return
	 */
	Integer updateVisitApply(DesignerVisitApply designerVisitApply);
	
	/**
	 * @param id
	 * @return
	 */
	DesignerVisitApply getDesignerVisitApplyById(Integer id);
}