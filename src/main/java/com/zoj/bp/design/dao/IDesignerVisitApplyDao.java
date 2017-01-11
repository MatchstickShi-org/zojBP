/**
 * 
 */
package com.zoj.bp.design.dao;

import java.util.List;

import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.design.vo.DesignerVisitApply;

/**
 * @author wangw
 *
 */
public interface IDesignerVisitApplyDao
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
	DatagridVo<DesignerVisitApply> getTodayDesignerVisitApplys(Pagination pagination,String designerName,Integer orderId,Integer... status);
	
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
	List<DesignerVisitApply> getTodayApplysByOrder(Integer orderId);
	/**
	 * @param id
	 * @return
	 */
	DesignerVisitApply getDesignerVisitApplyById(Integer id);

	
}