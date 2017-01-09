/**
 * 
 */
package com.zoj.bp.design.service;

import java.util.List;

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
	 */
	Integer addDesignerVisitApply(DesignerVisitApply designerVisitApply);
	/**
	 * @param pagination
	 * @param designerName
	 * @param orderId
	 * @param status
	 * @return
	 */
	DatagridVo<DesignerVisitApply> getAllDesignerVisitApply(Pagination pagination,String designerName,Integer orderId,Integer... status);
	
	/**
	 * 更新回访申请
	 * @param designerVisitApply
	 * @return
	 */
	Integer updateVisitApply(DesignerVisitApply designerVisitApply);
	/**
	 * 根据客户Id查询设计师回访申请记录
	 * @param orderId
	 * @return
	 */
	List<DesignerVisitApply> getDesignerVisitApplyByOrderId(Integer orderId);
	/**
	 * @param id
	 * @return
	 */
	DesignerVisitApply getDesignerVisitApplyById(Integer id);
}